<script setup>
	import { onMounted, ref } from "vue";
	import { mainContentsWithLimit, validateUser } from "../../js/axios/axios.js";
	import QuestionSample from "./body/questionsample.vue";
	import GallerySample from "./body/gallerysample.vue";
	import NotifySample from "./body/notifysample.vue";
	import BoardSample from "./body/boardsample.vue";
	import router from "../../js/router.js";

	const props = defineProps({ nickname: String, login: Boolean });

	const galleries = ref(new Map());
	const notifies = ref(new Map());
	const boards = ref(new Map());
	const questions = ref(new Map());

	/**
	 * home 에서 필요한 내용 받아오기
	 */
	async function mainPageAxios() {
		await mainContentsWithLimit()
			.catch((err) => console.log(err))
			.then((value) => {
				console.log(value?.data);
				galleries.value
					.set("content", value.data["gallery"]["results"])
					.set("total", value.data["gallery"]["total"]);
				notifies.value
					.set("content", value.data["notify"]["results"])
					.set("total", value.data["notify"]["total"]);
				questions.value
					.set("content", value.data["question"]["results"])
					.set("total", value.data["question"]["total"]);
				boards.value
					.set("content", value.data["board"]["results"])
					.set("total", value.data["board"]["total"]);
			});
	}

	async function toBoardDetail() {
		await validateUser()
			.then((value) => {
				console.log(value);
				if (
					(value ?? null) === null ||
					(value.data ?? null) === null ||
					value.data["nickname"] !== props.nickname
				) {
					router.push("login");
					return;
				}
				router.push({ name: "question", query: { inquiryHistory: "1" } });
			})
			.catch((err) => {
				if ((err["errorName"] ?? null) === null) {
					console.log(err);
					alert(err);
					return;
				}
				switch (err["errorName"]) {
					case "NEED_LOGIN_EXCEPTION":
					case "ILLEGAL_JWT_EXCEPTION":
						router.push("login");
						break;
					case "NEED_SIGN_IN_EXCEPTION":
						router.push("signIn");
						break;
				}
			});
	}

	onMounted(() => {
		// findUserNicknameAxios();
		mainPageAxios();
	});
</script>
<template>
	<div
		style="
			gap: 1.5em;
			display: grid;
			grid-template-columns: repeat(2, minmax(0, 1fr));
			grid-template-rows: repeat(2, minmax(0, 1fr));
			flex-basis: 80%;
		">
		<NotifySample v-bind:total="notifies.get('total')" v-bind:content="notifies.get('content')" />
		<BoardSample v-bind:content="boards.get('content')" v-bind:total="boards.get('total')" />
		<GallerySample
			v-bind:total="galleries.get('total')"
			v-bind:content="galleries.get('content')" />
		<QuestionSample
			v-bind:loggedIn="props.login"
			v-bind:question="questions.get('content')"
			v-bind:nickname="props.nickname"
			v-on:toBoardDetail="toBoardDetail"
			v-bind:total="questions.get('total')" />
	</div>
</template>
<style scoped></style>

<script setup>
	import { onMounted, ref } from "vue";
	import { mapQueryString, queryParser } from "../../utils/queryParsing.js";
	import router from "../../js/router.js";
	import axios from "../../js/axios/axios.js";
	import { submitDateToStr } from "../../utils/dateFunction.js";
	import ViewTemplate from "../global/viewtemplate.vue";
	import { jwtParser } from "../../utils/jwtparser.js";
	import Header from "../global/header.vue";

	const conditions = ref(queryParser(router.currentRoute.value.query));
	const content = ref({});

	onMounted(() => {
		findUserNicknameAxios();
		findContent();
	});
	const login = ref(false);
	const nickname = ref("");

	function logout() {
		jwtParser.remove();
		login.value = false;
		nickname.value = "";
	}
	async function findUserNicknameAxios() {
		if (jwtParser.validity(jwtParser.get()) === false) {
			nickname.value = null;
			login.value = false;
			return;
		}
		const success = function (value) {
			console.log(value?.data);
			if ((value ?? null) !== null && (value.data ?? null) !== null) {
				nickname.value = value.data["nickname"];
				login.value = true;
			}
		};
		const error = function (err) {
			console.log(err);
			alert(err);
			jwtParser.remove();
		};
		const query = queryString("/user/main", null);
		await axios.get({ path: query, onSuccess: success, onError: error });
	}
	async function findContent() {
		console.log(conditions.value);
		const query = mapQueryString("/notify/view", conditions.value);
		const success = function (value) {
			console.log(value.data);
			if ((value.data ?? null) !== null) content.value = value.data["contents"];
		};
		await axios.get({ path: query, onError: null, onSuccess: success });
	}
	/**
	 * viewTemplate 에 필요한 내용들을 map 으로 만들어준다.
	 * @returns {Map<any, any>}
	 */
	function templateContent() {
		const map = new Map();
		map
			.set("headTitle", "공지사항")
			.set("title", content.value["title"])
			.set("content", content.value["content"])
			.set("viewCount", content.value["viewCount"])
			.set("categoryName", content.value["categoryName"])
			.set("submitDate", submitDateToStr(content.value["submitDate"]))
			.set("nickname", content.value["nickname"]);
		return map;
	}
</script>

<template>
	<div style="display: flex; justify-content: center; width: 90%; min-width: 700px">
		<div style="display: flex; width: 90%; flex-direction: column">
			<Header v-bind:nickname="nickname" v-bind:login="login" v-on:logout="logout"></Header>
			<div
				style="
					display: flex;
					width: 100%;
					align-items: center;
					flex-direction: column;
					margin-top: 20px;
				">
				<div
					style="display: flex; flex-direction: column; gap: 10px; width: 80%"
					v-if="(content ?? null) !== null && Object.keys(content).length > 0">
					<ViewTemplate v-bind:content="templateContent()" />
				</div>
				<span class="buttons">
					<button
						class="goBack"
						type="button"
						v-on:click="router.push({ name: 'notify', query: conditions })">
						목록
					</button>
				</span>
			</div>
		</div>
	</div>
</template>

<style scoped></style>

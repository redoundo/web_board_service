<script setup>
	import { onMounted, ref } from "vue";
	import axios from "../../js/axios/axios.js";
	import SearchOptions from "../global/searchoptions.vue";
	import PageOptions from "../global/pageoptions.vue";
	import Pagination from "../global/pagination.vue";
	import GalleryContents from "./component/gallerycontents.vue";
	import { queryParser, queryString } from "../../utils/queryParsing.js";
	import router from "../../js/router.js";
	import { jwtParser } from "../../utils/jwtparser.js";
	import { checkString } from "../../utils/checkValid.js";

	const conditions = ref(queryParser(router.currentRoute.value.query));

	const categories = ref({});
	const total = ref(0);
	const contents = ref([]);
	const userId = ref(-1);

	onMounted(() => {
		galleryAxios();
	});

	async function galleryAxios() {
		const url = queryString("/gallery", conditions.value);
		const success = function (value) {
			console.log(value?.data);
			if ((value ?? null) !== null && (value.data ?? null) !== null) {
				contents.value = value.data["results"];
				total.value = value.data["total"];
				categories.value = value.data["categories"];
			}
		};
		await axios.get({ path: url, onError: null, onSuccess: success });
	}
	function changeCondition(what, how) {
		conditions.value.set(what, how);
	}
	/**
	 * jwt 토큰이 존재한다면 그걸로 userId 를 받아온다.
	 * 없다면 로그인이 필요하다고 알린 뒤 로그인 페이지로 이동한다.
	 * @returns {Promise<void>}
	 */
	async function checkUserByJwt() {
		if (jwtParser.validity(jwtParser.get()) === false) {
			userId.value = -1;
			alert("로그인이 필요한 서비스입니다.");
			router.push({ name: "login" });
			return;
		}
		const query = queryString("/user/main", null);
		const success = function (value) {
			console.log(value?.data);
			if ((value ?? null) !== null && (value.data ?? null) !== null) {
				userId.value = value.data["userId"];
				router.push({ name: "galleryWrite", query: conditions.value });
			}
		};
		const error = function (err) {
			console.log(err);
			alert(err);
			jwtParser.remove();
		};
		await axios.get({ path: query, onSuccess: success, onError: error });
	}
</script>

<template>
	<div style="display: flex; flex-direction: column; align-items: center; margin-top: 10px">
		<div style="display: flex; flex-direction: column; width: 95%; min-width: 665px">
			<span style="margin-bottom: 0.75rem; font-size: 15px; text-align: left">갤러리</span>
			<div>
				<SearchOptions
					v-bind:categories="categories"
					v-bind:conditions="conditions"
					v-on:changeCondition="changeCondition"
					v-on:emitAxios="galleryAxios" />
				<PageOptions v-on:changeCondition="changeCondition" v-bind:conditions="conditions" />
			</div>
			<div style="font-size: 11.5px; margin-bottom: 5px">
				<GalleryContents v-bind:contents="contents" v-bind:conditions="conditions" />
			</div>
			<span
				style="
					display: flex;
					flex-direction: row;
					justify-content: end;
					margin-top: 5px;
					padding-right: 15px;
				">
				<button
					v-if="checkString(jwtParser.get())"
					type="button"
					style="
						padding: 5px;
						padding-left: 7px;
						padding-right: 7px;
						font-size: 12.5px;
						font-weight: bold;
					"
					v-on:click="checkUserByJwt">
					> 글쓰러 가기 <span style="margin-left: 5px">+</span>
				</button>
			</span>
			<Pagination
				v-bind:page="conditions.get('page')"
				v-bind:total="total"
				v-on:changeCondition="changeCondition" />
		</div>
	</div>
</template>

<style scoped></style>

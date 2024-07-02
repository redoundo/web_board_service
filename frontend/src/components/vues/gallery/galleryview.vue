<script setup>
	import axios, { findContentByIdAxios } from "../../js/axios/axios.js";
	import ViewBtnSet from "../global/viewbtnset.vue";
	import { onMounted, ref } from "vue";
	import router from "../../js/router.js";
	import { queryParser, queryString } from "../../utils/queryParsing.js";
	import GalleryImages from "./component/galleryimages.vue";
	import { jwtParser } from "../../utils/jwtparser.js";
	import { submitDateToStr } from "../../utils/dateFunction.js";
	import ViewTemplate from "../global/viewtemplate.vue";
	import Header from "../global/header.vue";

	const conditions = ref(queryParser(router.currentRoute.value.query));
	const content = ref({});
	const files = ref({});
	const index = ref(0);

	onMounted(() => {
		findUserNicknameAxios();
		findContent();
	});

	const login = ref(false);
	const nickname = ref("");
	const user = ref(null);

	async function findUserNicknameAxios() {
		if (jwtParser.validity(jwtParser.get()) === false) {
			nickname.value = null;
			login.value = false;
			user.value = null;
			return;
		}
		const success = function (value) {
			console.log(value?.data);
			if ((value ?? null) !== null && (value.data ?? null) !== null) {
				user.value = value.data;
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
		await findContentByIdAxios(conditions.value.get("contentId"), "gallery")
			.then((value) => {
				console.log(value.data);
				if ((value.data ?? null) !== null) {
					content.value = value.data["contents"];
					files.value = value.data["files"];
				}
			})
			.catch((err) => console.log(err));
	}

	async function deleteContent() {
		if (jwtParser.validity(jwtParser.get()) === false) {
			alert("로그인이 필요합니다.");
			return;
		}
		const conditionQueries = { ...conditions.value };
		const success = function (value) {
			console.log(value.data);
			if (value.data) {
				alert("게시글이 삭제 되었습니다.");
				const { contentId, ...other } = conditionQueries;
				router.push({ name: "gallery", query: other });
			}
		};
		console.log(conditions.value);
		const query =
			queryString("/user/gallery/view/delete", null) +
			`?contentId=${conditions.value.get("contentId")}`;
		await axios.get({ path: query, onError: null, onSuccess: success });
	}

	function logout() {
		jwtParser.remove();
		login.value = false;
		nickname.value = "";
	}
	/**
	 *
	 * @param imgIndex 변경할 이미지의 index
	 */
	function changeImageIndex(imgIndex) {
		if (index.value !== imgIndex) index.value = imgIndex;
	}
	/**
	 * viewTemplate 에 필요한 내용들을 map 으로 만들어준다.
	 * @returns {Map<any, any>}
	 */
	function templateContent() {
		const map = new Map();
		map
			.set("headTitle", "갤러리")
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
					flex-basis: 80%;
				">
				<div
					style="display: flex; flex-direction: column; gap: 10px; width: 80%"
					v-if="(content ?? null) !== null && Object.keys(content).length > 0">
					<ViewTemplate v-bind:content="templateContent()" />

					<GalleryImages
						v-bind:image-index="index"
						v-bind:images="files"
						v-on:changeImageIndex="changeImageIndex" />
					<div
						style="
							text-wrap: wrap;
							word-break: break-all;
							border-width: 1px;
							padding: 1em;
							min-height: 80px;
							border: 2px solid gainsboro;
							text-align: left;
							font-size: 13px;
						">
						{{ content["content"] }}
					</div>
					<span class="buttons">
						<button
							class="goBack"
							type="button"
							v-on:click="router.push({ name: 'gallery', query: conditions })">
							목록
						</button>
						<button
							class="ok"
							type="button"
							v-if="
								user['userId'] === content['userId'] &&
								(user['userId'] ?? null) !== null &&
								jwtParser.validity(jwtParser.get())
							"
							v-on:click="router.push({ name: 'galleryUpdate', query: conditions })">
							수정
						</button>
						<button
							class="deleteButton"
							type="button"
							v-if="
								user['userId'] === content['userId'] &&
								(user['userId'] ?? null) !== null &&
								jwtParser.validity(jwtParser.get())
							"
							v-on:click="deleteContent">
							삭제
						</button>
					</span>
				</div>
				<div style="font-size: 11.5px; line-height: normal; margin: 5px" v-else>
					내용이 존재하지 않습니다.
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped></style>

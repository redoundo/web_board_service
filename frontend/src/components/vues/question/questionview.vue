<script setup>
	import axios, { findContentByIdAxios } from "../../js/axios/axios.js";
	import ViewBtnSet from "../global/viewbtnset.vue";
	import { onMounted, ref } from "vue";
	import router from "../../js/router.js";
	import { queryParser, mapQueryString } from "../../utils/queryParsing.js";
	import { submitDateToStr } from "../../utils/dateFunction.js";
	import ViewTemplate from "../global/viewtemplate.vue";
	import { jwtParser } from "../../utils/jwtparser.js";
	import Header from "../global/header.vue";

	const conditions = ref(queryParser(router.currentRoute.value.query));
	const content = ref({});
	const comment = ref({});

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
		await findContentByIdAxios(conditions.value.get("contentId"), "question")
			.then((value) => {
				console.log(value?.data);
				if ((value.data ?? null) !== null) {
					content.value = value.data["contents"];
					comment.value = value.data["comments"];
				}
			})
			.catch((err) => console.log(err));
	}

	async function deleteContent() {
		if (jwtParser.validity(jwtParser.get()) === false) {
			alert("로그인이 필요합니다.");
			return;
		}

		// 답변 완료된 상태라면 삭제 불가능하므로 경고만 반환.
		if (content.value["isAnswer"] === false)
			return alert("답변 완료된 문의 내용은 삭제할 수 없습니다.");

		const success = function (value) {
			console.log(value?.data);
			if (value.data) {
				alert("게시글이 삭제 되었습니다.");
				const { contentId, ...other } = conditions.value;
				router.push({ name: "question", query: other });
			}
		};

		const query = mapQueryString("/user/question/view/delete", conditions.value);
		await axios.post({ path: query, onSuccess: success, onError: null });
	}

	/**
	 * viewTemplate 에 필요한 내용들을 map 으로 만들어준다.
	 * @returns {Map<any, any>}
	 */
	function templateContent() {
		const map = new Map();
		map
			.set("headTitle", "문의 게시판")
			.set(
				"title",
				`${content.value["title"]}(${content.value["isAnswer"] ? "답변완료" : "미완료"})`,
			)
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
					style="
						display: flex;
						width: 100%;
						align-items: center;
						flex-direction: column;
						margin-top: 20px;
					"
					v-if="(content ?? null) !== null && content.size() > 0">
					<ViewTemplate v-bind:content="templateContent()" />
					<div style="text-wrap: wrap; word-break: break-all; border-width: 1px; padding: 1em">
						{{ content["content"] }}
					</div>
					<!-- 관리자 답변 시작 = 답변 내용이 존재하고 답변 완료 되었을 때만 존재.-->
					<div
						class="questionComment"
						v-if="content.value['isAnswer'] && (comment ?? null) !== null && comment.size() > 0">
						<span>
							<span>{{ comment["nickname"] }}</span>
							<span>{{ submitDateToStr(comment["submitDate"]) }}</span>
						</span>
						<div>{{ comment["comment"] }}</div>
					</div>
					<div class="questionComment" v-else>아직 등록된 답변이 없습니다.</div>
					<!--관리자 답변 끝-->
					<ViewBtnSet
						v-on:deleteContent="deleteContent"
						v-bind:table="'question'"
						v-bind:conditions="conditions" />
				</div>
				<div class="flex flex-col space-y-1" v-else>내용이 존재하지 않습니다.</div>
			</div>
		</div>
	</div>
</template>

<style scoped></style>

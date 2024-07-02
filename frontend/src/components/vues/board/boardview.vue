<script setup>
	import router from "../../js/router.js";
	import { onMounted, ref } from "vue";
	import staticAxios from "axios";
	import axios from "../../js/axios/axios.js";
	import { queryString, queryParser } from "../../utils/queryParsing.js";
	import { jwtParser } from "../../utils/jwtparser.js";
	import Comment from "./components/comment.vue";
	import ViewTemplate from "../global/viewtemplate.vue";
	import ViewBtnSet from "../global/viewbtnset.vue";
	import { submitDateToStr } from "../../utils/dateFunction.js";
	import Header from "../global/header.vue";
	const url = "https://xnp2tr132k.execute-api.ap-northeast-2.amazonaws.com/api-stage/api";
	// const url = "http://localhost:8080/api";

	const conditions = ref({ ...router.currentRoute.value.query });
	const content = ref({});
	const files = ref([]);
	const comments = ref([]);
	const newComment = ref("");
	const user = ref({});
	const login = ref(false);
	const nickname = ref("");

	onMounted(() => {
		findUserInfo();
		viewContent();
	});
	function logout() {
		jwtParser.remove();
		login.value = false;
		nickname.value = "";
	}
	/**
	 * 자유 게시판 내용 가져오는 역할.
	 */
	async function viewContent() {
		console.log(conditions.value);

		const query = queryString("/board/view", conditions.value);
		console.log(query);
		const onSuccess = function (val) {
			console.log(val?.data);
			if ((val ?? null) === null) return;
			content.value = val.data["contents"];
			if ((val.data["files"] ?? null) !== null && val.data["files"].length > 0)
				files.value = [...val.data["files"]];
			if ((val.data["comments"] ?? null) !== null && val.data["comments"].length > 0)
				comments.value = [...val.data["comments"]];
		};

		await axios.get({ path: query, onSuccess: onSuccess, onError: null });
	}

	/**
	 * jwt 토큰이 존재하면 사용자 정보를 가져온다.
	 */
	async function findUserInfo() {
		const token = jwtParser.get(); // jwt 토큰이 최소한 존재는 하는지 확인.
		if (jwtParser.validity(token) === false) {
			login.value = false;
			user.value = {};
			return;
		} // jwt 토큰이 존재하지 않으면 사용자 정보 가져오지 않음.

		const success = function (val) {
			console.log(val?.data);
			if ((val ?? null) === null || (val.data ?? null) === null) return;
			user.value = val.data;
			login.value = true;
			nickname.value = val.data["nickname"];
		};

		const error = function (err) {
			if ((err["errorName"] ?? null) === null) {
				alert(err);
				login.value = false;
				user.value = {};
				return;
			}
			switch (err["errorName"]) {
				case "EXPIRED_TOKEN_EXCEPTION":
					alert("다시 로그인 해주시기 바랍니다.");
					jwtParser.remove();
					login.value = false;
					user.value = {};
					break;
			}
		};

		await axios.get({
			path: url + "/user/main",
			onError: error,
			onSuccess: success,
		});
	}

	/**
	 * 입력한 댓글 저장.
	 */
	async function insertComment() {
		const token = jwtParser.get(); // jwt 토큰이 최소한 존재는 하는지 확인.
		if (!jwtParser.validity(token)) return; // jwt 토큰이 존재하지 않으면 저장하지도 않음.
		// 댓글 내용 존재하는지 확인
		if ((newComment?.value ?? null) === null || newComment.value.length === 0) return;
		console.log(content.value);

		// 댓글 객체 생성.
		const commentEntity = {
			postTable: 0,
			postId: content.value["boardId"],
			userId: user.value["userId"],
			nickname: user.value["nickname"],
			answer: newComment.value,
		};

		// axios 를 보낼 url
		const query = queryString("/user/board/view/comment", conditions.value);
		const success = function (val) {
			// 예외가 발생하지 않았다면 true 반환.
			if ((val?.data ?? null) === null) {
				alert("댓글 저장에 실패했습니다. 다시 시도해 주세요.");
				return;
			}
			console.log(val.data);
			// 저장되었다면 현재의 댓글 목록에 추가
			comments.value = [...comments.value, val.data];
			newComment.value = "";
			console.log(comments.value);
		};

		await axios.post({
			path: query,
			data: commentEntity,
			onSuccess: success,
			onError: null,
		});
	}

	/**
	 * 댓글 삭제
	 * @param item 삭제하려는 댓글의 내용
	 */
	async function deleteComment(item) {
		const token = jwtParser.get(); // jwt 토큰이 최소한 존재는 하는지 확인.
		console.log(item);
		if (jwtParser.validity(token) === false) return;
		if ((item ?? null) === null) return;
		const query = queryString("/user/board/view/comment/delete", conditions.value);
		const success = (val) => {
			console.log(val?.data);
			if ((val?.data ?? null) !== null && val.data) {
				comments.value = comments.value.filter(
					(comment) => comment["answerId"] !== item["answerId"],
				);
			}
		};
		await axios.post({ path: query, data: item, onSuccess: success, onError: null });
	}

	/**
	 * viewTemplate 에 필요한 내용들을 map 으로 만들어준다.
	 * @returns {Map<any, any>}
	 */
	function templateContent() {
		const map = new Map();
		map
			.set("headTitle", "자유 게시판")
			.set("title", content.value["title"])
			.set("content", content.value["content"])
			.set("viewCount", content.value["viewCount"])
			.set("categoryName", content.value["categoryName"])
			.set("submitDate", submitDateToStr(content.value["submitDate"]))
			.set("nickname", content.value["nickname"]);
		return map;
	}

	/**
	 * 파일 다운로드
	 * @param item 파일 정보
	 */
	async function download(item) {
		console.log(item);
		if ((item ?? null) === null) return;
		const data = {
			fileName: item["imageName"] + item["imageExtension"],
			filePath: item["imagePath"],
		};
		await staticAxios
			.post(url + "/board/view/download", data, {
				headers: { "Content-Type": "application/json" },
				responseType: "blob",
			})
			.catch((err) => console.log(err))
			.then((value) => {
				if ((value ?? null) === null || (value.data ?? null) === null) {
					alert("파일 다운로드에 실패하였습니다. 다시 시도해 주세요");
					return;
				}
				const viweDiv = document.getElementById("boardViewDiv");
				const a = document.createElement("a");
				const imageUrl = window.URL.createObjectURL(new Blob([value.data]));
				a.href = imageUrl;
				a.setAttribute("download", item["imageName"] + item["imageExtension"]);
				viweDiv.appendChild(a);
				a.click();
			});
	}

	/**
	 * 게시글 삭제
	 */
	async function deleteContent() {
		if (!jwtParser.validity(jwtParser.get())) return;

		const query = queryString("/user/board/view/delete", conditions.value);
		await axios.get({ path: query, onSuccess: null, onError: null });
	}
</script>

<template>
	<div style="display: flex; justify-content: center; width: 90%; min-width: 700px">
		<div style="display: flex; width: 80%; flex-direction: column">
			<Header v-bind:nickname="nickname" v-bind:login="login" v-on:logout="logout"></Header>
			<div
				id="boardViewDiv"
				style="display: flex; flex-direction: column; align-items: center; margin-top: 10px">
				<div
					style="display: flex; flex-direction: column; width: 95%; min-width: 665px"
					v-if="(content ?? null) !== null && (content['content'] ?? null) !== null">
					<div style="display: flex; flex-direction: column; gap: 10px; margin-bottom: 10px">
						<ViewTemplate v-bind:content="templateContent()" />

						<div
							style="
								margin-left: 5px;
								display: flex;
								flex-direction: column;
								padding: 5px;
								padding-top: 10px;
								padding-bottom: 10px;
								align-items: start;
							"
							v-if="(files ?? null) !== null && files.length > 0">
							<span style="margin-bottom: 5px" v-for="(item, index) in files" v-bind:key="index">
								<button type="button" v-on:click="download(item)">
									<i
										class="bi bi-download"
										style="-webkit-text-stroke-width: 0.5px; color: #3c3633"></i>
								</button>
								<button
									type="button"
									style="text-decoration: underline; margin-left: 5px"
									v-on:click="download(item)">
									{{ item["imageName"] }}{{ item["imageExtension"] }}
								</button>
							</span>
						</div>
					</div>
					<Comment
						v-model:comment="newComment"
						v-on:insertComment="insertComment"
						v-on:deleteComment="deleteComment"
						v-bind:authority="user"
						v-bind:comments="comments" />
					<span class="buttons">
						<button
							class="goBack"
							type="button"
							v-on:click="router.push({ name: 'board', query: conditions })">
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
							v-on:click="router.push({ name: 'boardUpdate', query: conditions })">
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

					<!-- <ViewBtnSet
						v-bind:conditions="queryParser(conditions)"
						v-bind:table="'board'"
						v-bind:login="(user['userId'] ?? null) !== null && jwtParser.validity(jwtParser.get())"
						v-on:deleteContent="deleteContent"
						v-bind:isOwner="user['userId'] === content['userId']" /> -->
				</div>
				<div style="display: flex; flex-direction: column" v-else>내용이 존재하지 않습니다.</div>
			</div>
		</div>
	</div>
</template>

<style scoped></style>

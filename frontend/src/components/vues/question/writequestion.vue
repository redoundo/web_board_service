<script setup>
	import axios from "../../js/axios/axios.js";
	import router from "../../js/router.js";
	import { ref, onMounted } from "vue";
	import { queryParser, queryString } from "../../utils/queryParsing.js";
	import { jwtParser } from "../../utils/jwtparser.js";
	import Header from "../global/header.vue";

	const conditions = ref(queryParser(router.currentRoute.value.query));
	const title = ref("");
	const content = ref("");
	const checked = ref(false);

	onMounted(() => {
		findUserNicknameAxios();
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

	async function insertContent() {
		if ((title.value ?? null) === null || title.value.length > 100)
			return alert("제목을 규칙에 맞게 작성해주세요");
		if ((content.value ?? null) === null || content.value.length > 4000)
			return alert("내용을 규칙에 맞게 작성해주세요");

		if (jwtParser.validity(jwtParser.get()) === false) {
			alert("로그인이 필요합니다.");
			return router.push({ name: "gallery", query: { ...conditions.value } });
		}

		const success = function (value) {
			console.log(value?.data);
			router.push({ name: "question", query: { ...conditions.value } });
		};
		const error = function (err) {
			if ((err["errorName"] ?? null) === null) {
				console.log(err);
				alert(err);
				return;
			}
			console.log(err);
			alert(err["errorMessage"]);
		};
		const map = new Map()
			.set("title", title.value)
			.set("content", content.value)
			.set("isLocked", checked.value);
		console.log(map);

		const url = queryString("/user/question/write/insert", conditions.value);
		await axios.post({ path: url, data: map, onSuccess: success, onError: error });
	}

	/**
	 * bind 된 값을 변경
	 * @param name 변경이 필요한 ref 의 이름
	 * @param value 변경할 값.
	 */
	function setValue(name, value) {
		switch (name) {
			case "title":
				title.value = value;
				break;
			case "content":
				content.value = value;
				break;
			case "checked":
				checked.value = !checked.value;
				break;
		}
	}
	function confirmCancel() {
		if (confirm("작성을 취소하시겠습니까?"))
			return router.push({ name: "question", query: router.currentRoute.value.query });
	}
</script>

<template>
	<div style="display: flex; justify-content: center; width: 90%; min-width: 700px">
		<div style="display: flex; width: 90%; flex-direction: column">
			<Header v-bind:nickname="nickname" v-bind:login="login" v-on:logout="logout"></Header>
			<div class="writeFirstBlock" style="align-items: center">
				<div style="width: 88%; display: flex; flex-direction: column">
					<span class="writeHeadTitle" style="text-align: start; align-self: start">
						문의 게시판
					</span>
					<span class="aDetail">
						<span class="aDetailName">제목*</span>
						<span class="aDetailValue">
							<input
								style="padding: 5px 135px 5px 10px; font-size: small; flex-grow: 1"
								type="text"
								v-bind:value="title"
								placeholder="제목을 입력해주세요"
								v-on:change="setValue('title', $event.target.value)"
								required
								maxlength="99" />
						</span>
					</span>
					<span class="aDetail">
						<span class="aDetailName">내용*</span>
						<span class="aDetailValue">
							<textarea
								v-bind:value="content"
								v-on:change="setValue('content', $event.target.value)"
								required
								maxlength="3999"
								style="
									font-size: 12px;
									outline: none;
									resize: none;
									height: 150px;
									padding: 5px;
									flex-grow: 1;
								"
								class="grow">
							</textarea>
						</span>
					</span>
					<span class="aDetail">
						<span class="aDetailName">비밀글</span>
						<span
							class="aDetailValue"
							style="border-bottom: 2px solid gainsboro; padding: 2px 0px 5px 10px">
							<input
								style="min-width: 18px; min-height: 18px; margin-top: 5px"
								type="checkbox"
								v-bind:value="checked"
								v-on:click="setValue('checked', $event.target.value)" />
						</span>
					</span>
					<span class="buttons">
						<button class="ok" type="button" v-on:click="insertContent">등록</button>
						<button class="goBack" type="button" v-on:click="confirmCancel">취소</button>
					</span>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped></style>

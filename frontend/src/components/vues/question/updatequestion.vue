<script setup>
	import axios from "../../js/axios/axios.js";
	import router from "../../js/router.js";
	import { onMounted, ref } from "vue";
	import { jwtParser } from "../../utils/jwtparser.js";
	import { queryParser, queryString, mapQueryString } from "../../utils/queryParsing.js";

	const title = ref("");
	const content = ref("");
	const questionContent = ref({});
	const checked = ref(false);
	const conditions = ref(queryParser(router.currentRoute.value.query));

	onMounted(() => {
		getContent();
	});

	async function updateQuestion() {
		if (jwtParser.validity(jwtParser.get()) === false) {
			alert("로그인이 필요 합니다.");
			const { contentId, ...other } = conditions.value;
			router.push({ name: "question", query: other });
			return;
		}

		const map = new Map()
			.set("title", title.value)
			.set("content", content.value)
			.set("isLocked", checked.value)
			.set("userId", questionContent.value["userId"])
			.set("questionId", questionContent.value["questionId"]);

		const success = function (value) {
			console.log(value?.data);
			if ((value ?? null) !== null && (value.data ?? null) !== null) {
				if (value.data) {
					alert("게시글이 변경 되었습니다.");
					const { contentId, ...other } = conditions.value;
					router.push({ name: "question", query: other });
				} else alert("게시글 변경에 실패 하였습니다. 다시 시도해 주세요.");
			}
		};

		const query = queryString("/user/question/view/update", conditions.value);
		await axios.post({ path: query, data: map, onSuccess: success, onError: null });
	}

	function confirmCancel() {
		if (confirm("작성을 취소하시겠습니까?")) {
			const { contentId, ...other } = conditions.value;
			return router.push({
				name: "question",
				query: other,
			});
		}
	}

	async function getContent() {
		const success = function (value) {
			console.log(value?.data);
			if ((value ?? null) !== null && (value.data ?? null) !== null) {
				title.value = value.data["contents"]["title"];
				content.value = value.data["contents"]["content"];
				checked.value = value.data["contents"]["isLocked"];
				questionContent.value = value.data;
			}
		};
		const query = mapQueryString("/user/question/update", conditions.value);
		await axios.get({ path: query, onSuccess: success, onError: null });
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
				checked.value = value;
				break;
		}
	}
</script>

<template>
	<div class="flex flex-col">
		<span class="text-lg mb-4" style="padding-left: 5px">문의 게시판</span>
		<div>
			<span class="aDetail">
				<span class="aDetailName">제목*</span>
				<span class="aDetailValue">
					<input
						class="grow"
						style="
							font-size: small;
							padding-top: 2.2px;
							padding-bottom: 2.2px;
							padding-right: 135px;
							padding-left: 10px;
						"
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
						style="font-size: 12px; outline: none; resize: none; height: 150px; padding: 5px"
						class="grow">
					</textarea>
				</span>
			</span>
			<span class="aDetail">
				<span class="aDetailName">비밀글</span>
				<span class="aDetailValue">
					<input
						style="min-width: 15px; min-height: 15px; margin-top: 5px"
						type="checkbox"
						v-bind:value="checked"
						v-on:click="setValue('checked', $event.target.value)" />
				</span>
			</span>
			<span class="buttons">
				<button class="ok" type="button" v-on:click="updateQuestion">등록</button>
				<button class="goBack" type="button" v-on:click="confirmCancel">취소</button>
			</span>
		</div>
	</div>
</template>

<style scoped></style>

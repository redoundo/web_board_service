<script setup>
	import axios from "../../js/axios/axios.js";
	import staticAxios from "axios";
	import { onMounted, ref } from "vue";
	import Upload from "../global/upload.vue";
	import router from "../../js/router.js";
	import { jwtParser } from "../../utils/jwtparser.js";
	import { queryParser, queryString } from "../../utils/queryParsing.js";
	import Header from "../global/header.vue";

	const url = "https://xnp2tr132k.execute-api.ap-northeast-2.amazonaws.com/api-stage/api";
	// const url = "http://localhost:8080/api";

	const conditions = ref(queryParser(router.currentRoute.value.query));
	const categories = ref([]);
	const files = ref([]);
	const lengths = ref(1);

	const title = ref("");
	const category = ref("null");
	const content = ref("");

	onMounted(() => {
		getCategories();
		findUserNicknameAxios();
		console.log(category.value);
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

	async function getCategories() {
		const success = function (value) {
			console.log(value?.data);
			if ((value ?? null) !== null && (value.data ?? null) !== null) categories.value = value.data;
		};

		await axios.get({
			path: url + "/user/board/write",
			onSuccess: success,
			onError: null,
		});
	}

	function deleteUploadedFile(index) {
		files.value = files.value.filter((val) => files.value.indexOf(val) !== index);
		lengths.value = lengths.value - 1;
		console.log(files.value);
		console.log(lengths.value);
	}

	function fileUpload(file, index) {
		// if (file.size() > 2 * 1024 * 1024) files.value = [...files.value, null];
		console.log(file, index);
		console.log(files.value);
		// const fileValue = [...files.value].splice(index, 0, file);
		files.value = [...files.value, file];
		console.log(files.value);
	}

	function range(size) {
		return new Array(size).fill(1).map((val, index) => val + index);
	}

	function addFileInput() {
		lengths.value = lengths.value + 1;
	}

	/**
	 * 내용 저장
	 */
	async function insertContent() {
		const token = jwtParser.get();
		if (!jwtParser.validity(token)) return;

		if ((title.value ?? null) === null || title.value.length > 100)
			return alert("제목을 규칙에 맞게 입력 해주세요");
		if ((category.value ?? null) === null) return alert("카테고리를 설정 해주세요");
		if ((content.value ?? null) === null || content.value.length > 4000)
			return alert("내용을 규칙에 맞게 입력 해주세요");
		if ((files.value ?? null) === null || files.value.length < 1)
			return alert("최소 한 개 이상의 파일이 필요합니다.");

		const success = function (val) {
			console.log(val?.data);
			const { contentId, ...other } = conditions.value;
			router.push({ name: "board", query: other });
		};

		const error = function (err) {
			if ((err["errorName"] ?? null) === null) return console.log(err);

			const errNames = [
				"FAILED_AUTHORIZED_EXCEPTION",
				"NEED_SIGN_IN_EXCEPTION",
				"UNAUTHORIZED_ACCESS_EXCEPTION",
			];

			if (errNames.includes(err["errorName"])) {
				alert(err["statusMessage"]);
			}
		};

		const query = queryString("/user/board/write/insert", {
			...conditions.value,
		});
		const board = {
			postTable: 0,
			title: title.value,
			categoryId: category.value,
			content: content.value,
		};
		const formData = new FormData();
		formData.append("title", title.value);
		formData.append("categoryId", category.value);
		formData.append("content", content.value);
		// formData.set("files", files.value);
		formData.append("postTable", 0);
		// formData.append("content", board);
		// formData.append("content", new Blob([JSON.stringify(board)], { type: "application/json" }));
		const fileData = new Blob([...files.value], { type: "multipart/form-data" });

		// staticAxios.defaults.headers.post["Content-Type"] = "multipart/form-data";
		staticAxios.defaults.headers.common["Authorization"] = jwtParser.get();
		// console.log(formData);
		let fileArray = [];
		for (let f of files.value) {
			console.log(f);
			// formData.append("files", new Blob([f], { type: "multipart/form-data" }));
			formData.append("files", f, "files");
		}

		console.log(formData.get("content"));
		await staticAxios
			.post(
				query,
				{
					postTable: 0,
					title: title.value,
					categoryId: category.value,
					content: content.value,
					files: [...files.value],
				},
				{ headers: { "Content-Type": "multipart/form-data" } },
			)
			.then((value) => success(value))
			.catch((err) => error(err));
	}

	function confirmCancel() {
		if (confirm("작성을 취소하시겠습니까?")) {
			const { contentId, ...other } = conditions.value;
			return router.push({ name: "board", query: other });
		}
	}

	/**
	 * 변경된 내용 저장
	 * @param {String} name
	 * @param {*} value
	 */
	function setValue(name, value) {
		switch (name) {
			case "title":
				title.value = value;
				break;
			case "content":
				content.value = value;
				break;
			case "category":
				category.value = value;
				break;
		}
	}
</script>

<template>
	<div style="display: flex; justify-content: center; width: 90%; min-width: 700px">
		<div style="display: flex; width: 90%; flex-direction: column">
			<Header v-bind:nickname="nickname" v-bind:login="login" v-on:logout="logout"></Header>
			<div class="writeFirstBlock">
				<span class="writeHeadTitle" style="text-align: left">자유 게시판</span>
				<div>
					<span class="aDetail">
						<span class="aDetailName"> 분류* </span>
						<span class="aDetailValue">
							<select
								style="flex-grow: 1; max-width: 200px; padding: 3px; padding-left: 5px"
								v-on:change="setValue('category', $event.target.value)"
								v-if="(categories ?? null) !== null && categories.length > 0"
								v-bind:value="category">
								<option value="null" v-bind:selected="categories.includes(category) === false">
									분류 선택
								</option>
								<option
									v-for="(item, index) in categories"
									v-bind:key="index"
									v-bind:value="item['categoryId']"
									v-bind:selected="category === item['categoryId']">
									{{ item["categoryName"] }}
								</option>
							</select>
							<select
								style="flex-grow: 1; max-width: 200px; padding: 3px; padding-left: 5px"
								v-else>
								<option value="null" selected>분류 선택</option>
							</select>
						</span>
					</span>
					<span class="aDetail">
						<span class="aDetailName"> 제목* </span>
						<span class="aDetailValue">
							<input
								style="
									font-size: 12px;
									padding-top: 2.2px;
									padding-bottom: 2.2px;
									padding-left: 10px;
									flex-grow: 1;
								"
								v-bind:value="title"
								v-on:change="setValue('title', $event.target.value)"
								type="text"
								placeholder="제목을 입력해주세요"
								required
								maxlength="99" />
						</span>
					</span>
					<span class="aDetail">
						<span class="aDetailName"> 내용* </span>
						<span class="aDetailValue">
							<textarea
								required
								name="content"
								maxlength="3999"
								v-bind:value="content"
								v-on:change="setValue('content', $event.target.value)"
								placeholder="내용을 입력해 주세요"
								style="height: 90px; flex-grow: 1"
								class="grow">
							</textarea>
						</span>
					</span>
					<span class="aDetail">
						<span class="aDetailName">첨부</span>
						<div
							style="
								display: flex;
								flex-direction: column;
								row-gap: 5px;
								flex-basis: 85%;
								background: white;
								text-align: left;
								padding: 6px;
								padding-left: 12px;
							">
							<span style="font-size: 12px; padding-bottom: 5px">
								jpg, gif, png 파일만 업로드 가능하며, 크기는 최대 2MB 입니다.
							</span>
							<div style="display: flex; flex-direction: column">
								<Upload
									v-for="index in range(lengths)"
									v-bind:key="index"
									v-bind:index="files.length - 1"
									v-bind:table="'board'"
									v-on:fileUpload="fileUpload"
									v-on:deleteUploadedFile="deleteUploadedFile" />
							</div>
							<div style="padding-top: 6px">
								<button
									style="
										font-size: 12px;
										background-color: white;
										border-color: #3c3633;
										border-width: 1px;
										border-radius: 3px;
										width: 40px;
										height: 22px;
									"
									type="button"
									v-if="lengths < 20 && files.length < 20"
									v-on:click="addFileInput">
									추가
								</button>
							</div>
						</div>
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

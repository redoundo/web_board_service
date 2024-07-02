<script setup>
	import axios, { downloadFile } from "../../js/axios/axios.js";
	import staticAxios from "axios";
	import { onMounted, ref } from "vue";
	import Upload from "../global/upload.vue";
	import router from "../../js/router.js";
	import { queryParser, queryString } from "../../utils/queryParsing.js";
	import { jwtParser } from "../../utils/jwtparser.js";
	import Header from "../global/header.vue";

	const conditions = ref(queryParser(router.currentRoute.value.query));
	const categories = ref([]);

	const boardContent = ref({});
	const files = ref([]);
	const uploadedFiles = ref([]);
	const lengths = ref(1);

	const title = ref("");
	const category = ref(null);
	const content = ref("");

	onMounted(() => {
		console.log(conditions.value);
		findContent();
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

	/**
	 * 수정할 내용 가져오기
	 */
	function findContent() {
		const token = jwtParser.get();
		if (jwtParser.validity(token) === false) {
			alert("로그인이 필요합니다.");
			return router.push({ name: "board", query: { ...conditions.value } });
		}

		const success = function (val) {
			console.log(val?.data);
			if ((val?.data ?? null) === null) return;
			boardContent.value = val.data["contents"];
			if ((val.data["files"] ?? null) !== null && val.data["files"].length > 0)
				uploadedFiles.value = [...val.data["files"]];
			if ((val.data["categories"] ?? null) !== null && val.data["categories"].length > 0)
				categories.value = val.data["categories"];
		};
		const error = function (err) {
			if ((err["errorName"] ?? null) === null) return console.log(err);

			const errNames = [
				"NEED_LOGIN_EXCEPTION",
				"NEED_SIGN_IN_EXCEPTION",
				"UNAUTHORIZED_ACCESS_EXCEPTION",
			];
			if (errNames.includes(err["errorName"])) {
				alert(err["statusMessage"]);
				return router.push({ name: "board", query: { ...conditions.value } });
			}
		};

		const query =
			queryString("/user/board/update", null) + `?contentId=${conditions.value.get("contentId")}`;
		axios.get({ path: query, onSuccess: success, onError: error });
	}

	function deleteUploadedFile(index) {
		files.value = files.value.filter((val) => files.value.indexOf(val) !== index);
		lengths.value = lengths.value - 1;
	}

	function fileUpload(file) {
		if (file.size() > 2 * 1024 * 1024) files.value = [...files.value, null];
		files.value = [...files.value, file];
	}

	function range(size) {
		return new Array(size).fill(1).map((val, index) => val + index);
	}

	function addFileInput() {
		lengths.value = lengths.value + 1;
	}

	/**
	 * 내용 변경
	 */
	function updateContents() {
		const token = jwtParser.get();
		if (jwtParser.validity(token) === false) {
			alert("로그인이 필요합니다.");
			return router.push({ name: "board", query: { ...conditions.value } });
		}

		const success = function (val) {
			console.log(val?.data);
			alert("수정 되었습니다.");
			router.push({ name: "board", query: router.currentRoute.value.query });
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
				return router.push({ name: "board", query: { ...conditions.value } });
			}
		};
		const uploadedFileIds = function () {
			if (uploadedFiles.value.length > 0) {
				const fileIds = [];
				for (let f of uploadedFiles.value) {
					fileIds.push(f["imageId"]);
				}
				return fileIds;
			}
			return [];
		};

		const formData = new FormData();
		formData.set("title", title.value);
		formData.set("categoryId", category.value);
		formData.set("content", content.value);
		formData.set("files", files.value);
		formData.set("images", uploadedFileIds());
		formData.set("boardId", boardContent.value["boardId"]);
		formData.set("postTable", 0);

		staticAxios.defaults.headers.common["Content-Type"] = "multipart/form-data";

		const query = queryString("/user/board/update/update", { ...conditions.value });
		axios.post({
			path: query,
			data: formData,
			onSuccess: success,
			onError: error,
		});
	}

	function confirmCancel() {
		if (confirm("작성을 취소하시겠습니까?"))
			return router.push({ name: "boardView", query: router.currentRoute.value.query });
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
			<div
				class="updateFirstBlock"
				style="display: flex; flex-direction: column; align-items: center; flex-basis: 80%">
				<div style="text-align: left; width: 95%; display: flex; flex-direction: column">
					<span class="updateHeadTitle">자유 게시판</span>
					<span class="aDetail">
						<span class="aDetailName"> 분류* </span>
						<span class="aDetailValue">
							<select
								style="flex-grow: 1; max-width: 200px"
								v-on:change="setValue('category', $event.target.value)"
								v-if="(categories ?? null) !== null && categories.length > 0"
								v-bind:value="
									(boardContent['categoryId'] ?? null) !== null ? boardContent['categoryId'] : null
								">
								<option value="null">분류 선택</option>
								<option
									v-for="(item, index) in categories"
									v-bind:key="index"
									v-bind:value="item['categoryId']"
									v-bind:selected="category === item['categoryId']">
									{{ item["categoryName"] }}
								</option>
							</select>
							<select style="flex-grow: 1; max-width: 200px" v-else>
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
								v-bind:value="boardContent['title']"
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
								v-bind:value="boardContent['content']"
								v-on:change="setValue('content', $event.target.value)"
								placeholder="내용을 입력해 주세요"
								style="height: 90px; flex-grow: 1">
							</textarea>
						</span>
					</span>
					<span class="aDetail">
						<span class="aDetailName">첨부</span>
						<div
							style="
								flex-grow: 1;
								background: white;
								display: flex;
								flex-direction: column;
								gap: 10px;
								align-items: start;
								padding: 10px;
							">
							<span style="font-size: 12px; padding-bottom: 5px">
								jpg, gif, png 파일만 업로드 가능하며, 크기는 최대 2MB 입니다.
							</span>
							<div style="display: flex; flex-direction: column; margin: 5px; gap: 5px">
								<div
									style="margin-left: 5px"
									v-if="(uploadedFiles ?? null) !== null && uploadedFiles.length > 0">
									<span
										v-for="(item, index) in uploadedFiles"
										v-bind:key="index"
										style="
											display: flex;
											flex-direction: row;
											justify-content: space-evenly;
											margin-bottom: 10px;
											margin-top: 5px;
										">
										<input
											name="files"
											type="file"
											style="font-size: 11px"
											accept=".jpg,.png,.gif"
											v-bind:value="item['imagePath']" />
										<button
											style="
												background-color: white;
												border-color: #3c3633;
												border-width: 1px;
												border-radius: 3px;
												width: 20px;
												height: 20px;
											"
											v-on:click="uploadedFiles = uploadedFiles.filter((elem) => elem !== item)"
											type="button">
											X
										</button>
										<button
											v-on:click="downloadFile(item['imageName'], item['imagePath'])"
											v-text="item['imageName']"></button>
									</span>
								</div>
								<Upload
									v-for="index in range(lengths)"
									v-bind:key="index"
									v-bind:index="files.length - 1"
									v-bind:table="'board'"
									v-on:fileUpload="fileUpload"
									v-on:deleteUploadedFile="deleteUploadedFile" />
								<span style="padding-top: 5px">
									<button
										v-if="
											lengths + uploadedFiles.length < 20 &&
											files.length + uploadedFiles.length < 20
										"
										v-on:click="addFileInput"
										type="button "
										style="
											font-size: 12px;
											background-color: white;
											border-color: #3c3633;
											border-width: 1px;
											border-radius: 3px;
											width: 40px;
											height: 22px;
										">
										추가
									</button>
								</span>
							</div>
						</div>
					</span>
					<span class="buttons">
						<button class="ok" type="button" v-on:click="updateContents">수정</button>
						<button class="goBack" type="button" v-on:click="confirmCancel">취소</button>
					</span>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped></style>

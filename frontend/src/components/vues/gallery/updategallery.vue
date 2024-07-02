<script setup>
	import axios, { downloadFile } from "../../js/axios/axios.js";
	import { onMounted, ref } from "vue";
	import Upload from "../global/upload.vue";
	import router from "../../js/router.js";
	import { queryParser, queryString } from "../../utils/queryParsing.js";
	import { jwtParser } from "../../utils/jwtparser.js";
	import Header from "../global/header.vue";

	const conditions = ref(queryParser(router.currentRoute.value.query));
	const categories = ref([]);

	const galleryContent = ref({});
	const files = ref([]);
	const uploadedFiles = ref([]);
	const lengths = ref(1);

	const title = ref("");
	const category = ref(null);
	const content = ref("");

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

	/**
	 * 수정할 내용 가져오기
	 */
	function findContent() {
		const token = jwtParser.get();
		if (jwtParser.validity(token) === false) {
			alert("로그인이 필요합니다.");
			return router.push({ name: "gallery", query: { ...conditions.value } });
		}

		const success = function (val) {
			console.log(val?.data);
			if ((val ?? null) === null) return;
			galleryContent.value = val.data["contents"];
			if ((val.data["files"] ?? null) !== null && val.data["files"].length > 0)
				files.value = [...val.data["files"]];
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
				return router.push({ name: "gallery", query: { ...conditions.value } });
			}
		};

		const query = queryString("/user/gallery/update", { ...conditions.value });
		axios.get({ path: query, onSuccess: success, onError: error });
	}

	function deleteUploadedFile(index) {
		files.value = files.value.filter((val) => files.value.indexOf(val) !== index);
		lengths.value = lengths.value - 1;
	}

	function fileUpload(file, index) {
		// if (file.size() > 1024 * 1024) files.value = [...files.value, null];
		const fileValue = [...files.value].splice(index, 0, file);
		files.value = fileValue;
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
			return router.push({ name: "gallery", query: { ...conditions.value } });
		}

		const success = function (val) {
			console.log(val?.data);
			alert("수정 되었습니다.");
			router.push({ name: "gallery", query: router.currentRoute.value.query });
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
				return router.push({ name: "gallery", query: { ...conditions.value } });
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
		formData.set("postTable", 1);

		staticAxios.defaults.headers.common["Content-Type"] = "multipart/form-data";

		const query = queryString("/user/gallery/update/update", { ...conditions.value });
		axios.post({
			path: query,
			data: formData,
			onSuccess: success,
			onError: error,
		});
	}

	function confirmCancel() {
		if (confirm("작성을 취소하시겠습니까?"))
			return router.push({ name: "gallery", query: { ...conditions.value } });
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
	function upload(event, index) {
		const { files } = event.target;
		if (files.length < 1) return;
		if (files[0].size() > 1024 * 1024) return alert("1MB 를 초과 하는 파일은 업로드 불가능합니다.");
		fileUpload(files[0], index);
		const fileReader = new FileReader();
		fileReader.readAsDataURL(files[0]);
		fileReader.onload = function (ev) {
			if (ev.loaded) {
				const uploaded = [...uploadedFiles.value];
				uploaded[index]["imagePath"] = ev.target.result;
				uploadedFiles.value = uploaded;
			}
		};
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
					<span class="updateHeadTitle" style="margin-bottom: 5px; font-size: 17px">갤러리</span>
					<span class="aDetail">
						<span class="aDetailName">분류*</span>
						<span class="aDetailValue">
							<select
								required
								style="flex-grow: 1; max-width: 200px"
								v-if="(categories ?? null) !== null && categories.length > 0"
								v-bind:value="category"
								v-on:change="setValue('category', $event.target.value)">
								<option value="null" v-bind:selected="category === null">분류 선택</option>
								<option
									v-for="(item, index) in categories"
									v-bind:selected="category === item['categoryId']"
									v-bind:key="index"
									v-bind:value="item['categoryId']">
									{{ item["categoryName"] }}
								</option>
							</select>
							<select style="flex-grow: 1; max-width: 200px" v-else>
								<option value="null" selected>분류 선택</option>
							</select>
						</span>
					</span>
					<span class="aDetail">
						<span class="aDetailName">제목*</span>
						<span class="aDetailValue">
							<input
								v-bind:value="title"
								type="text"
								placeholder="제목을 입력해주세요"
								v-on:change="setValue('title', $event.target.value)"
								required
								maxlength="99"
								style="
									font-size: 12px;
									padding-top: 3px;
									padding-bottom: 3px;
									padding-right: 135px;
									padding-left: 10px;
									flex-grow: 1;
								" />
						</span>
					</span>
					<span class="aDetail">
						<span class="aDetailName">내용*</span>
						<span class="aDetailValue">
							<textarea
								v-bind:value="content"
								v-on:change="setValue('content', $event.target.value)"
								required
								maxlength="4000"
								placeholder="내용을 입력해 주세요"
								style="
									font-size: 12px;
									outline: none;
									resize: none;
									height: 90px;
									flex-grow: 1;
									padding: 5px;
								"></textarea>
						</span>
					</span>
					<div>
						<span class="aDetail" style="border-bottom: 2px solid gainsboro">
							<span class="aDetailName">갤러리 이미지</span>
							<div class="aDetailValue">
								<div style="display: flex; flex-direction: column; margin: 5px; gap: 5px">
									<span style="font-size: 12px; padding-bottom: 5px">
										.jpg,.gif,.png 파일만 파일 사이즈 2MB 까지 업로드 가능합니다.
									</span>
									<div
										style="margin-left: 5px"
										v-if="(uploadedFiles ?? null) !== null && uploadedFiles.length > 0">
										<span style="display: flex; flex-direction: column">
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
												<img
													style="min-width: 40px; min-height: 40px; max-width: 50px"
													v-bind:src="'http://localhost:5000/@fs/' + item['imagePath']"
													alt="업로드 이미지" />
												<div
													style="
														display: flex;
														flex-direction: column;
														gap: 5px;
														padding-left: 15px;
													">
													<span>
														<input
															type="file"
															accept=".jpg,.png,.gif"
															style="font-size: 12px"
															v-on:change="upload($event, index)" />
														<button
															v-on:click="
																uploadedFiles = uploadedFiles.filter((elem) => elem !== item)
															"
															type="button"
															style="
																background-color: white;
																border-color: #3c3633;
																border-width: 1px;
																border-radius: 3px;
																width: 20px;
																height: 20px;
															">
															X
														</button>
													</span>
													<span>
														<button
															type="button"
															v-on:click="downloadFile(item['imageName'], item['imagePath'])"
															v-text="item['imageName']"></button>
													</span>
												</div>
											</span>
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
					</div>
				</div>
				<span class="buttons">
					<button class="ok" type="button" v-on:click="updateContents">수정</button>
					<button class="goBack" type="button" v-on:click="confirmCancel">취소</button>
				</span>
			</div>
		</div>
	</div>
</template>

<style scoped></style>

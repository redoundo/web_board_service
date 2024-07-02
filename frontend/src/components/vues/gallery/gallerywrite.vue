<script setup>
	import axios from "../../js/axios/axios.js";
	import staticAxios from "axios";
	import { onMounted, ref } from "vue";
	import Upload from "../global/upload.vue";
	import router from "../../js/router.js";
	import { queryParser, queryString } from "../../utils/queryParsing.js";
	import { jwtParser } from "../../utils/jwtparser.js";
	import Header from "../global/header.vue";

	const url = "https://xnp2tr132k.execute-api.ap-northeast-2.amazonaws.com/api-stage/api";
	// const url = "http://localhost:8080/api";

	const conditions = ref(queryParser(router.currentRoute.value.query));
	const categories = ref({});
	const files = ref([]);
	const lengths = ref(1);

	const title = ref("");
	const category = ref("null");
	const content = ref("");

	onMounted(() => {
		getCategories();
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

	async function getCategories() {
		const success = function (value) {
			console.log(value?.data);
			if ((value ?? null) !== null && (value.data ?? null) !== null)
				if ((value.data ?? null) !== null) categories.value = value.data;
		};

		await axios.get({
			path: url + "/user/gallery/write",
			onSuccess: success,
			onError: null,
		});
	}

	function deleteUploadedFile(index) {
		files.value = files.value.filter((val) => files.value.indexOf(val) !== index);
		lengths.value = lengths.value - 1;
	}

	function fileUpload(file, index) {
		console.log(file);
		// if (file.size() > 1024 * 1024) files.value = [...files.value, null];
		if (index < 1) {
			const fileValue = [...files.value, file];
			files.value = fileValue;
			console.log(files.value, fileValue);
			return;
		}
		const fileValue = [...files.value].splice(index, 0, file);
		files.value = fileValue;
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
		if (jwtParser.validity(token) === false) return;

		if ((title.value ?? null) === null || title.value.length > 100)
			return alert("제목을 규칙에 맞게 입력 해주세요");
		if ((category.value ?? null) === null) return alert("카테고리를 설정 해주세요");
		if ((content.value ?? null) === null || content.value.length > 4000)
			return alert("내용을 규칙에 맞게 입력 해주세요");
		if ((files.value ?? null) === null || files.value.length < 1)
			return alert("최소 한 개 이상의 파일이 필요합니다.");

		const conditionsQueries = { ...conditions.value };
		const success = function (val) {
			console.log(val?.data);
			const { contentId, ...other } = conditionsQueries;
			router.push({ name: "gallery", query: other });
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
				return router.push({ name: "gallery", query: conditionsQueries });
			}
		};

		const query = queryString("/user/gallery/write/insert", conditionsQueries);
		const formData = new FormData();
		formData.set("title", title.value);
		formData.set("categoryId", category.value);
		formData.set("content", content.value);
		for (let f of files.value) {
			formData.append("files", f);
		}
		// formData.set("files", [...files.value]);
		formData.set("postTable", 1);
		console.log(formData.get("files"));
		staticAxios.defaults.headers.common["Content-Type"] = "multipart/form-data";
		staticAxios.defaults.headers.common["Authorization"] = jwtParser.get();
		await axios.post({
			path: query,
			data: formData,
			onSuccess: success,
			onError: error,
			header: "multipart/form-data",
		});
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

	function confirmCancel() {
		if (confirm("작성을 취소하시겠습니까?"))
			return router.push({ name: "gallery", query: { ...conditions.value } });
	}
</script>

<template>
	<div style="display: flex; justify-content: center; width: 90%; min-width: 700px">
		<div style="display: flex; width: 90%; flex-direction: column">
			<Header v-bind:nickname="nickname" v-bind:login="login" v-on:logout="logout"></Header>
			<div class="writeFirstBlock">
				<div style="width: 88%; display: flex; flex-direction: column">
					<span class="writeHeadTitle" style="text-align: start; align-self: start">갤러리</span>
					<div
						style="
							border-top-width: 2px;
							padding-left: 10px;
							display: flex;
							flex-direction: column;
						">
						<span class="aDetail">
							<span class="aDetailName">분류*</span>
							<span class="aDetailValue">
								<select
									required
									style="
										flex-grow: 1;
										font-size: 12px;
										padding-top: 3px;
										padding-bottom: 3px;
										padding-right: 135px;
										padding-left: 10px;
										max-width: 250px;
									"
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
								<select
									style="
										flex-grow: 1;
										font-size: 12px;
										padding-top: 3px;
										padding-bottom: 3px;
										padding-right: 135px;
										padding-left: 10px;
										max-width: 250px;
									"
									v-else>
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
										flex-grow: 1;
										font-size: 12px;
										padding-top: 3px;
										padding-bottom: 3px;
										padding-right: 135px;
										padding-left: 10px;
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
										flex-grow: 1;
										font-size: 12px;
										outline: none;
										resize: none;
										height: 90px;

										padding: 5px;
									"
									class="grow"></textarea>
							</span>
						</span>
						<div style="border-bottom: 2px solid gainsboro">
							<span class="aDetail">
								<span class="aDetailName">갤러리 이미지</span>
								<div class="aDetailValue">
									<div
										style="
											display: flex;
											flex-direction: column;
											margin: 5px;
											gap: 5px;
											flex-grow: 1;
											align-items: start;
										">
										<span style="font-size: 12px; padding-bottom: 5px">
											.jpg,.gif,.png 파일만 파일 사이즈 2MB 까지 업로드 가능합니다.
										</span>
										<div style="display: flex; margin-left: 5px; flex-direction: column; gap: 5px">
											<Upload
												v-for="index in range(lengths)"
												v-bind:key="index"
												v-bind:index="files.length - 1"
												v-bind:table="'board'"
												v-on:fileUpload="fileUpload"
												v-on:deleteUploadedFile="deleteUploadedFile" />
										</div>
										<span style="padding-top: 5px">
											<button
												v-if="lengths < 20 && files.length < 20"
												v-on:click="addFileInput"
												type="button"
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
				</div>
				<span class="buttons">
					<button class="ok" type="button" v-on:click="insertContent">등록</button>
					<button class="goBack" type="button" v-on:click="confirmCancel">취소</button>
				</span>
			</div>
		</div>
	</div>
</template>

<style scoped></style>

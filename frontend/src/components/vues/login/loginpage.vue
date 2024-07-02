<script setup>
	import router from "../../js/router.js";
	import Header from "../global/header.vue";
	import axios from "../../js/axios/axios";
	import { loginAxios } from "../../js/axios/axios.js";
	import { jwtParser } from "../../utils/jwtparser.js";
	import { queryString } from "../../utils/queryParsing";
	import { ref, onMounted } from "vue";
	import { checkString } from "../../utils/checkValid.js";

	const password = ref(null);
	const id = ref(null);

	async function getLoggedIn() {
		await loginAxios(id.value, password.value)
			.then((value) => {
				console.log(value.data);
				console.log(value);
				console.log(value.headers["JSESSIONID"]);
				jwtParser.set(value.data["accessToken"]); // 로그인 완료된 뒤, 반환된 jwt 토큰을 쿠키나 로컬 스토리지에 저장.
				router.push({ name: "main" }); // 로그인이 완료되면 메인 페이지로 이동.
			})
			.catch((err) => {
				console.log(err);
				if ((err["errorName"] ?? null) !== null) {
					alert(err["statusMessage"]);
					router.push({ name: "signIn" });
				}
			});
	}
	function inputPassword(val) {
		if (checkString(val)) password.value = val;
		console.log(password.value);
	}
	function inputId(val) {
		if (checkString(val)) id.value = val;
		console.log(id.value);
	}

	const login = ref(false);
	const nickname = ref("");

	onMounted(() => {
		findUserNicknameAxios();
	});

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
</script>

<template>
	<div style="display: flex; justify-content: center; width: 90%; min-width: 700px">
		<div style="display: flex; width: 80%; flex-direction: column; align-items: center">
			<Header v-bind:nickname="nickname" v-bind:login="login" v-on:logout="logout"></Header>
			<div
				style="
					display: flex;
					width: 50%;
					flex-direction: column;
					margin-top: 40px;
					gap: 10px;
					align-items: center;
				">
				<div style="font-size: 1.125rem; font-weight: bold; margin-bottom: 30px">로그인</div>
				<div
					style="display: flex; flex-direction: column; gap: 20px; width: 60%; align-items: center">
					<div
						style="
							display: flex;
							flex-direction: column;
							justify-content: center;
							width: 100%;
							gap: 10px;
						">
						<label style="text-align: left; font-size: 13.5px"> 아이디 </label>
						<input
							style="padding: 7px 10px"
							placeholder="아이디를 입력해 주세요."
							type="text"
							v-bind:value="id"
							v-on:focusout="inputId($event.target.value)" />
					</div>
					<div
						style="
							display: flex;
							flex-direction: column;
							justify-content: center;
							width: 100%;
							gap: 10px;
						">
						<label style="text-align: left; font-size: 13.5px">비밀번호</label>
						<input
							style="padding: 7px 10px"
							type="password"
							placeholder="비밀번호를 입력해 주세요"
							v-bind:value="password"
							v-on:focusout="inputPassword($event.target.value)" />
					</div>
				</div>
				<span class="buttons" style="margin-top: 5.5em">
					<button type="button" class="ok" v-on:click="getLoggedIn">로그인</button>
					<button type="button" class="ok" v-on:click="router.push({ name: 'signIn' })">
						회원가입
					</button>
				</span>
			</div>
		</div>
	</div>
</template>

<style scoped></style>

<script setup>
	import axios, { signInAxios, canUseThisId } from "../../js/axios/axios.js";
	import { jwtParser } from "../../utils/jwtparser.js";
	import { checkString } from "../../utils/checkValid.js";
	import Header from "../global/header.vue";
	import { queryString } from "../../utils/queryParsing";
	import { ref, onMounted } from "vue";
	import router from "../../js/router.js";

	const duplicateChecked = ref(null);
	const id = ref(null);
	const passwordCheck = ref(null);
	const password = ref(null);
	const newNickname = ref(null);

	function letSignIn() {
		if ((duplicateChecked.value ?? null) === null || duplicateChecked.value === false)
			alert("아이디 중복확인을 먼저 진행해주세요.");
		if (password.value !== passwordCheck.value)
			alert("비밀번호가 동일하지 않습니다. 다시 확인해주세요.");
		console.log(id.value);
		console.log(password.value);
		console.log(newNickname.value);
		signInAxios(id.value, password.value, newNickname.value)
			.catch((err) => console.log(err))
			.then((value) => {
				console.log(value.data);
				if ((value.data ?? null) !== null) {
					alert("회원가입에 성공했습니다.");
					jwtParser.set(value.data["accessToken"]);
					router.push({ name: "main" });
				} else {
					alert("다시 한번 시도해주세요.");
				}
			});
	}

	async function checkDuplicate() {
		await canUseThisId(id.value)
			.then((value) => {
				console.log(value.data);
				if (value.data === false) {
					alert("사용할 수 없는 아이디입니다.");
					duplicateChecked.value = false;
					newNickname.value = null;
				}
				duplicateChecked.value = true;
			})
			.catch((err) => {
				console.log(err);
				duplicateChecked.value = null;
			});
	}

	function inputChange(name, value) {
		switch (name) {
			case "id":
				if (checkString(value)) id.value = value;
				break;
			case "password":
				if (checkString(value)) password.value = value;
				break;
			case "passwordCheck":
				if (checkString(value)) passwordCheck.value = value;
				break;
			case "newNickname":
				if (checkString(value)) newNickname.value = value;
		}
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
			<Header v-bind:newNickname="newNickname" v-bind:login="login" v-on:logout="logout"></Header>
			<div
				style="
					display: flex;
					width: 50%;
					flex-direction: column;
					margin-top: 40px;
					gap: 10px;
					align-items: center;
				">
				<div style="font-size: 1.125rem; font-weight: bold; margin-bottom: 30px">회원가입</div>
				<div
					style="
						display: flex;
						flex-direction: column;
						gap: 20px;
						width: 70%;
						align-items: center;
						min-width: 270px;
					">
					<div
						style="
							display: flex;
							flex-direction: column;
							justify-content: center;
							width: 100%;
							gap: 10px;
						">
						<label style="text-align: left; font-size: 13.5px">
							아이디
							<span
								v-if="duplicateChecked ?? null"
								v-bind:class="duplicateChecked ? 'agree' : 'dismiss'"
								v-text="
									duplicateChecked ? '사용 가능한 아이디 입니다.' : '사용 불가능한 아이디 입니다.'
								"></span>
						</label>
						<span style="display: flex; flex-direction: row; justify-content: space-between">
							<input
								type="text"
								placeholder="아이디를 입력해 주세요."
								style="padding: 7px 10px; width: 70%"
								v-bind:value="id"
								v-on:change="inputChange('id', $event.target.value)" />
							<button
								type="button"
								style="background-color: gray; color: white; padding: 5px 10px; border-radius: 7px"
								v-on:click="checkDuplicate">
								중복확인
							</button>
						</span>
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
							type="password"
							style="padding: 7px 10px"
							v-bind:value="password"
							v-on:change="inputChange('password', $event.target.value)"
							placeholder="비밀번호" />
					</div>
					<div
						style="
							display: flex;
							flex-direction: column;
							justify-content: center;
							width: 100%;
							gap: 10px;
						">
						<label style="text-align: left; font-size: 13.5px">비밀번호 확인</label>
						<input
							type="password"
							style="padding: 7px 10px"
							v-bind:value="passwordCheck"
							v-on:change="inputChange('passwordCheck', $event.target.value)"
							placeholder="비밀번호를 입력해 주세요" />
					</div>
					<div
						style="
							display: flex;
							flex-direction: column;
							justify-content: center;
							width: 100%;
							gap: 10px;
						">
						<label style="text-align: left; font-size: 13.5px">닉네임</label>
						<input
							type="text"
							style="padding: 7px 10px"
							v-bind:value="newNickname"
							v-on:change="inputChange('newNickname', $event.target.value)"
							placeholder="닉네임을 입력해 주세요" />
					</div>
				</div>
				<span style="margin-top: 4em">
					<button type="button" class="ok" v-on:click="letSignIn">회원가입</button>
				</span>
			</div>
		</div>
	</div>
</template>

<style scoped>
	.dismiss {
		color: crimson;
		margin-left: 10px;
	}
	.agree {
		color: forestgreen;
		margin-left: 10px;
	}
</style>

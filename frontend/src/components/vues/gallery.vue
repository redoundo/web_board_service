<script setup>
	import Header from "./global/header.vue";
	import Gallery from "./gallery/gallery.vue";
	import axios from "../js/axios/axios";
	import { jwtParser } from "../utils/jwtparser.js";
	import { queryString } from "../utils/queryParsing";
	import { onMounted, ref } from "vue";

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
		<div style="display: flex; width: 80%; flex-direction: column">
			<Header v-bind:nickname="nickname" v-bind:login="login" v-on:logout="logout"></Header>
			<Gallery />
		</div>
	</div>
</template>

<style scoped></style>

<script setup>
	import { checkString } from "../../utils/checkValid.js";
	import { jwtParser } from "../../utils/jwtparser.js";
	import router from "../../js/router.js";

	const props = defineProps({ nickname: String, login: Boolean });
	const emits = defineEmits(["logout"]);

	function goto(boardName) {
		const href = document.location.href;
		if (boardName !== "login" && boardName !== "signIn") {
			return router.push({ name: boardName });
		} else {
			if (href.includes("login") == false && href.includes("signIn") == false) {
				return router.push({ name: boardName });
			}
		}
	}
</script>

<template>
	<div
		style="
			min-width: 665px;
			display: flex;
			flex-direction: column;
			padding: 0px 10px;
			flex-basis: 20%;
		">
		<div style="margin-top: 30px; text-align: center; padding: 10px">
			<button
				style="font-size: 13.5px; font-size: 25px; font-family: 'Bebas Neue'"
				type="button"
				v-on:click="goto('main')">
				web-board-project
			</button>
		</div>
		<div
			style="
				display: flex;
				font-size: 0.75rem;
				flex-direction: column;
				row-gap: 0.5rem;
				padding-top: 0.5rem;
			">
			<span
				style="
					border-bottom-width: 2px;
					padding-left: 1em;
					padding-right: 1em;
					display: flex;
					flex-direction: row;
					justify-content: space-between;
					row-gap: 0.5rem;
					margin-top: 0.25rem;
					padding-bottom: 0.25rem;
					margin-left: 0.25rem;
				">
				<span>
					<button
						type="button"
						style="font-size: 13.5px; margin-right: 0.625rem"
						v-on:click="goto('notify')">
						공지사항
					</button>
					<button
						type="button"
						style="font-size: 13.5px; margin-right: 0.625rem"
						class="headbutton"
						v-on:click="goto('board')">
						자유게시판
					</button>
					<button
						type="button"
						style="font-size: 13.5px; margin-right: 0.625rem"
						class="headbutton"
						v-on:click="goto('gallery')">
						갤러리
					</button>
					<button
						type="button"
						style="font-size: 14px"
						class="headbutton"
						v-on:click="goto('question')">
						문의 게시판
					</button>
				</span>
				<span v-if="checkString(nickname) === false || checkString(jwtParser.get()) === false">
					<button
						style="font-size: 13.5px; margin-right: 5px"
						type="button"
						v-on:click="goto('login')">
						로그인
					</button>
					<span>/</span>
					<button
						style="font-size: 13.5px; margin-left: 5px"
						type="button"
						v-on:click="goto('signIn')">
						회원가입
					</button>
				</span>
				<span
					style="
						font-size: 13.5px;
						display: flex;
						justify-content: end;
						padding-right: 0.5rem;
						margin-right: 5px;
						margin-left: 5px;
						align-items: center;
					"
					v-if="checkString(nickname) && checkString(jwtParser.get())">
					<span>{{ nickname }}님 안녕하세요!</span>
					<button type="button" class="text-xs" v-on:click="emits('logout')">로그아웃</button>
				</span>
			</span>
		</div>
		<hr style="padding: 0px; margin: 0px" />
	</div>
</template>

<style scoped>
	.headbutton::before {
		content: " | ";
		margin-right: 1em;
		color: #3c3633;
	}
</style>

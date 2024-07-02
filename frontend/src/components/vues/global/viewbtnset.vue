<script setup>
	import router from "../../js/router.js";
	const props = defineProps({
		conditions: Map,
		table: String,
		login: Boolean,
		isOwner: Boolean,
	});
	const emits = defineEmits(["deleteContent"]);
</script>

<template>
	<span class="buttons">
		<button
			class="goBack"
			type="button"
			v-on:click="router.push({ name: props.table, query: props.conditions })">
			목록
		</button>
		<button
			class="ok"
			type="button"
			v-if="props.isOwner && props.login"
			v-on:click="router.push({ name: `${props.table}Update`, query: props.conditions })">
			수정
		</button>
		<button
			class="deleteButton"
			type="button"
			v-if="props.isOwner && props.login"
			v-on:click="emits('deleteContent')">
			삭제
		</button>
	</span>
</template>

<style scoped></style>

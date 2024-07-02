<script setup>
	import { onMounted, ref } from "vue";
	import { jwtParser } from "../../utils/jwtparser.js";
	import { dateFunction } from "../../utils/dateFunction.js";
	import { checkString } from "../../utils/checkValid.js";
	const props = defineProps({ user: Object, isChecked: Boolean, conditions: Map });
	const emits = defineEmits(["changeCondition", "emitAxios"]);

	const keyword = ref(null);

	onMounted(() => {
		console.log(props.categories);
		console.log(props.conditions);
		console.log(props.user);
		console.log((props.user ?? null) !== null && props.user["userId"] > 0);
	});

	function searching() {
		emits("changeCondition", "keyword", keyword.value);
		emits("emitAxios");
	}

	function changeInput(val) {
		if (checkString(val)) keyword.value = val;
	}
</script>

<template>
	<span
		style="
			border-width: 2px;
			padding: 0.5em;
			min-width: 650px;
			display: flex;
			flex-direction: row;
			justify-content: space-between;
			align-items: center;
		">
		<span
			style="
				flex-basis: 55%;
				max-width: 350px;
				min-width: 308px;
				padding-right: 8px;
				display: flex;
				flex-direction: row;
				justify-content: space-between;
				align-items: center;
			">
			<span>등록일시</span>
			<input
				style="padding: 3px 10px"
				type="date"
				v-on:change="emits('changeCondition', 'start', $event.target.value)"
				v-bind:value="
					(props.conditions.get('start') ?? null) !== null
						? props.conditions.get('start')
						: dateFunction('m', -1)
				" />
			<span>~</span>
			<input
				style="padding: 3px 10px"
				type="date"
				v-on:change="emits('changeCondition', 'end', $event.target.value)"
				v-bind:value="
					(props.conditions.get('end') ?? null) !== null
						? props.conditions.get('end')
						: dateFunction('m', null)
				" />
		</span>
		<span
			style="
				padding-left: 5px;
				display: flex;
				flex-direction: row;
				align-items: center;
				flex-basis: 8%;
				flex-grow: 1;
				padding-right: 10px;
				gap: 10px;
			"
			v-if="
				(props.user ?? null) !== null && props.user['userId'] > 0 && checkString(jwtParser.get())
			">
			<label style="flex-basis: 70%; text-wrap: nowrap"> 나의 문의 내역 </label>
			<input
				style="width: 20px; height: 20px; flex-basis: 30%"
				type="checkbox"
				v-bind:checked="isChecked"
				v-on:change="emits('changeCondition', 'inquiryHistory', $event.target.value)" />
		</span>
		<span
			style="flex-basis: 37%; display: flex; flex-direction: row; justify-content: space-around">
			<input
				type="search"
				style="
					flex-grow: 1;
					border-width: 2px;
					padding: 2px 10px;
					max-width: 300px;
					max-height: 30px;
				"
				placeholder="제목 or 내용"
				v-bind:value="keyword"
				v-on:change="changeInput($event.target.value)" />
			<button
				style="
					background-color: gainsboro;
					color: white;
					padding: 4px 20px;
					max-height: 30px;
					min-width: 65px;
				"
				type="button"
				v-on:click="searching">
				검색
			</button>
		</span>
	</span>
</template>

<style scoped></style>

<script setup>
	import { submitDateToStr } from "../../utils/dateFunction.js";

	const props = defineProps({ submitDate: Array });

	/**
	 * 제공된 날짜가 현재의 날짜에서 -7을 한 날짜보다 더 작다면(과거라면) false. 크다면 true 반환.
	 */
	function compareDate() {
		const now = new Date();
		const submitDateString = submitDateToStr(props.submitDate);
		const submittedDate = Date.parse(submitDateString);
		const beforeSevenDate = new Date(now.getFullYear(), now.getMonth(), now.getDate() - 7);
		return submittedDate >= beforeSevenDate;
	}
</script>
<template>
	<span
		style="margin-left: 1em; text-decoration: underline; font-weight: bolder; flex-basis: 20%"
		v-if="compareDate">
		new
	</span>
</template>

<style scoped></style>

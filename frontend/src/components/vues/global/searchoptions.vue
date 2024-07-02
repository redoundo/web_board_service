<script setup>
	import { onMounted, ref } from "vue";
	import { dateFunction } from "../../utils/dateFunction.js";
	import { checkString } from "../../utils/checkValid.js";

	const props = defineProps({ categories: Array, conditions: Map });
	const emits = defineEmits(["changeCondition", "emitAxios"]);

	const keyword = ref(null);

	onMounted(() => {
		console.log(props.categories);
		console.log(props.conditions);
	});

	function searching() {
		emits("changeCondition", "keyword", keyword.value);
		emits("emitAxios");
	}

	function changeInput(val) {
		if (checkString(val)) keyword.value = val;
	}
	function isQuestion() {
		return document.location.href.includes("question");
	}
</script>

<template>
	<div>
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
				style="flex-basis: 8%; flex-grow: 2"
				v-if="(props.categories ?? null) !== null && props.categories.length > 0">
				<select
					style="border-width: 2px; width: 80%; padding: 3px 10px; min-width: 90px"
					v-on:change="emits('changeCondition', 'categoryId', $event.target.value)">
					<option value="null">전체 분류</option>
					<option
						v-for="(item, index) in props.categories"
						v-bind:key="index"
						v-bind:value="item.categoryId"
						v-bind:selected="props.conditions.get('categoryId') === item.categoryId">
						{{ item.categoryName }}
					</option>
				</select>
			</span>
			<span style="flex-basis: 8%; flex-grow: 2" v-else>
				<select style="border-width: 2px; width: 80%; padding: 3px 10px; min-width: 90px">
					<option value="null">전체 분류</option>
				</select>
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
	</div>
</template>

<style scoped></style>

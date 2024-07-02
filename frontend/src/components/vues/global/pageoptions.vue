<script setup>
	const props = defineProps({ conditions: Map });
	const emits = defineEmits(["changeCondition"]);
	const columnDict = {
		등록일시: "submitDate",
		분류: "categoryId",
		제목: "title",
		조회수: "viewCount",
	};
</script>

<template>
	<span style="display: flex; justify-content: space-between; padding: 10px">
		<span style="border-width: 2px; padding: 3px">
			<select
				style="padding: 4px 6px"
				v-on:change="emits('changeCondition', 'maxCount', $event.target.value)">
				<option
					v-for="item in [10, 20, 30, 40, 50]"
					v-bind:value="item"
					v-bind:key="item"
					v-bind:selected="
						(props.conditions.get('maxCount') ?? null) !== null
							? props.conditions.get('maxCount') === item
							: item === 20
					">
					{{ item }}개 씩 보기
				</option>
			</select>
		</span>
		<span
			style="
				display: flex;
				flex-direction: row;
				justify-content: space-around;
				align-items: center;
				margin-left: 1em;
			">
			<span>
				<span
					style="
						border-width: 2px;
						padding: 3px;
						padding-left: 4px;
						padding-right: 4px;
						margin-right: 10px;
					">
					<select
						style="padding: 5px 7px"
						v-on:change="
							emits('changeCondition', 'orderByColumn', columnDict[$event.target.value])
						">
						<option
							v-for="item in ['등록일시', '분류', '제목', '조회수']"
							v-bind:key="item"
							v-bind:selected="
								(props.conditions.get('orderByColumn') ?? null) !== null
									? props.conditions.get('orderByColumn') === columnDict[item]
									: item === '등록일시'
							">
							{{ item }}
						</option>
					</select>
				</span>
				<span style="border-width: 2px; padding: 3px; padding-left: 4px; padding-right: 4px">
					<select
						style="padding: 5px 7px"
						v-on:change="emits('changeCondition', 'orderByDesc', $event.target.value)">
						<option
							v-for="(item, index) in ['내림차순', '오름차순']"
							v-bind:value="index"
							v-bind:selected="
								(props.conditions.get('orderByDesc') ?? null) !== null
									? props.conditions.get('orderByDesc') === index
									: item === '내림차순'
							">
							{{ item }}
						</option>
					</select>
				</span>
			</span>
		</span>
	</span>
</template>

<style scoped></style>

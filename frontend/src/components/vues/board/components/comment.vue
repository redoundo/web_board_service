<script setup>
	import { submitDateToStr, numberSubmitDate } from "../../../utils/dateFunction.js";

	const props = defineProps({ comments: Array, authority: Object });
	const emits = defineEmits(["insertComment", "deleteComment"]);
	const comment = defineModel("comment", { required: true });
</script>

<template>
	<div style="background-color: gainsboro; font-size: 11px">
		<span
			style="padding: 5px; display: flex; flex-direction: row; gap: 10px"
			v-if="(props.authority ?? null) !== null && Object.keys(props.authority).length > 0">
			<textarea
				v-model="comment"
				style="flex-basis: 90%; outline: none; resize: none; font-size: 11px; flex-grow: 1"
				placeholder="댓글을 입력해 주세요"></textarea>
			<button
				style="
					flex-basis: 10%;
					background-color: white;
					border-color: #3c3633;
					font-size: 11px;
					max-width: 50px;
					min-width: 40px;
					max-height: 40px;
					min-height: 42.8px;
				"
				type="button"
				@click="emits('insertComment')">
				등록
			</button>
		</span>
		<div v-if="(props.comments ?? null) !== null && Object.keys(props.comments).length > 0">
			<div
				style="
					font-size: 11px;
					text-align: left;
					display: flex;
					flex-direction: column;
					padding: 10px;
					border-bottom: 1px solid white;
				"
				v-for="(item, index) in props.comments"
				v-bind:key="index">
				<span style="display: flex; flex-direction: row; justify-content: space-between">
					<span style="margin-right: 5px">
						<span style="font-weight: bold; margin-right: 5px" v-text="item['nickname']"></span>
						<span
							v-text="
								(item['submitDate'] ?? null) === null
									? numberSubmitDate()
									: submitDateToStr(item['submitDate'])
							">
						</span>
					</span>
					<button
						style="padding-right: 5px; font-weight: bold"
						v-on:click="emits('deleteComment', item)"
						v-if="
							(props.authority ?? null) !== null &&
							Object.keys(props.authority).length > 0 &&
							item['userId'] === props.authority['userId']
						">
						삭제
					</button>
				</span>
				<span style="word-break: break-all; padding-top: 3px" v-text="item['answer']"></span>
			</div>
		</div>
	</div>
</template>

<style scoped></style>

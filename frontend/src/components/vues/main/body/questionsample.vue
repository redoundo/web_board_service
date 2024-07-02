<script setup>
	import { jwtParser } from "../../../utils/jwtparser.js";
	import { checkString } from "../../../utils/checkValid.js";
	import router from "../../../js/router.js";
	import newIcon from "../../global/newicon.vue";

	const props = defineProps({
		question: Array,
		total: Number,
		loggedIn: Boolean,
		nickname: String,
	});
	const emits = defineEmits(["toBoardDetail"]);
	function goto(id) {
		router.push({ name: "questionView", query: { contentId: id } });
	}
</script>

<template>
	<div
		style="
			max-width: 550px;
			display: flex;
			flex-direction: column;
			align-items: center;
			width: 100%;
			margin: 10px;
		">
		<div style="width: 90%">
			<span
				style="
					display: flex;
					flex-direction: row;
					text-align: center;
					padding: 0.25rem;
					justify-content: space-between;
					min-width: 300px;
					background-color: darkgrey;
					color: white;
					align-items: center;
					width: 100%;
				">
				<button
					style="flex-basis: 20%"
					class="text-xs"
					type="button"
					v-if="props.loggedIn && checkString(jwtParser.get())"
					v-on:click="emits('toBoardDetail')">
					나의 문의 내역
				</button>
				<span style="flex-basis: 60%; font-weight: bold">문의 게시판</span>
				<span style="flex-basis: 20%; font-size: 0.75rem">
					<button style="color: white" type="button" v-on:click="router.push({ name: 'question' })">
						더보기 +
					</button>
				</span>
			</span>
			<span
				style="
					border-bottom: 2px solid gainsboro;
					display: flex;
					flex-direction: row;
					text-align: center;
					font-weight: bold;
					padding: 0.25rem;
					width: 100%;
				">
				<span style="flex-basis: 15%">번호</span>
				<span style="flex-basis: 15%"></span>
				<span style="flex-basis: 70%">제목</span>
			</span>
			<div
				style="width: 100%"
				v-if="(props.question ?? null) !== null && props.question.length > 0">
				<span
					style="
						display: flex;
						flex-direction: row;
						padding: 5px;
						text-align: center;
						border-bottom: 1px solid gainsboro;
						justify-content: space-between;
						gap: 5px;
					"
					v-for="(item, index) in props.question"
					v-bind:key="index">
					<span style="flex-basis: 20%">{{ props.total - index }}</span>
					<!--        <button type="button" v-on:click="goto(item['questionId'])">-->
					<!--          {{item['title']}}({{item['isAnswered']? '답변완료': "미답변"}})-->
					<!--        </button>-->
					<span style="flex-basis: 70%; text-align: left; display: flex; flex-direction: row">
						<span
							style="
								flex-basis: 80%;
								overflow: hidden;
								text-overflow: ellipsis;
								white-space: nowrap;
							">
							<span
								style="
									text-align: left;
									display: flex;
									flex-direction: row;
									justify-content: left;
									gap: 5px;
								">
								<span style="flex-basis: 80%; text-align: left">
									<router-link
										style="margin-right: 1em"
										class="titleLink"
										v-bind:to="{ name: 'questionView', query: { contentId: item['questionId'] } }">
										{{ item["title"] }}({{ item["isAnswered"] ? "답변완료" : "미답변" }})
									</router-link>
									<newIcon v-bind:submitDate="item['submitDate']" />
								</span>
								<span style="flex-basis: 20%" v-if="item['isLocked']">
									<i class="bi bi-lock-fill"></i>
								</span>
							</span>
						</span>
					</span>
				</span>
			</div>
			<div style="padding: 5px; padding-top: 10px" v-else>문의 게시글이 존재하지 않습니다.</div>
		</div>
	</div>
</template>

<style scoped></style>

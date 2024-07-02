<script setup>
	import NewIcon from "../global/newicon.vue";
	import { submitDateToStr } from "../../utils/dateFunction.js";
	import router from "../../js/router.js";

	const props = defineProps({
		contents: Array,
		total: Number,
		conditions: Map,
		tableName: String,
		user: Object,
	});
	function goto(item) {
		if (
			props.tableName === "question" &&
			(user ?? null) !== null &&
			user["userId"] === item["userId"]
		) {
			router.push({
				name: "questionView",
				params: { ...props.conditions, contentId: item["questionId"] },
			});
		}
	}
</script>

<template>
	<div>
		<span
			style="
				padding: 5px;
				display: flex;
				flex-direction: row;
				text-align: center;
				border-bottom: 2px solid gainsboro;
				border-top: 2px solid gainsboro;
			">
			<span style="flex-basis: 7%">번호</span>
			<span style="flex-basis: 8%">분류</span>
			<span style="flex-basis: 45%">제목</span>
			<span style="flex-basis: 8%">조회</span>
			<span style="flex-basis: 19%">등록일시</span>
			<span style="flex-basis: 13%">등록자</span>
		</span>
		<div
			style="margin-top: 5px; display: flex; flex-direction: column; gap: 5px; font-size: 13px"
			v-if="(props.contents ?? null) !== null && props.contents.length > 0">
			<span
				style="
					display: flex;
					flex-direction: row;
					text-align: center;
					border-bottom: 1px solid gainsboro;
				"
				v-for="(item, index) in contents"
				v-bind:key="index"
				v-bind:class="
					props.tableName === 'notify' && Object.keys(item).includes('fixOnTop') && item['fixOnTop']
						? 'fixOnTop'
						: 'notFixOnTop'
				">
				<span
					v-if="
						props.tableName === 'notify' && (item['fixOnTop'] ?? null) !== null && item['fixOnTop']
					"
					style="flex-basis: 7%">
				</span>
				<span v-else style="flex-basis: 7%; text-decoration: underline">
					{{
						total - ((props.conditions.get("page") - 1) * props.conditions.get("maxCount") - index)
					}}
				</span>
				<span style="flex-basis: 8%; text-decoration: underline">{{ item["categoryName"] }}</span>
				<span
					style="
						text-align: left;
						flex-basis: 45%;
						display: flex;
						flex-direction: row;
						overflow: hidden;
						text-overflow: ellipsis;
						white-space: nowrap;
					">
					<span style="overflow: hidden; text-overflow: ellipsis">
						<!-- question 일 경우에는 비밀글 설정이 되어있으면 본인만 들어갈 수 있게 설정 -->
						<!-- question 용 link 시작 -->
						<button
							style="text-decoration: underline; margin-right: 5px"
							v-if="props.tableName === 'question'"
							v-on:click="goto(item)">
							{{ item["title"] }}
						</button>
						<!-- question 용 link 끝 -->
						<router-link
							style="text-decoration: underline; margin-right: 5px"
							v-bind:to="{
								name: props.tableName + 'View',
								query: { ...props.conditions, contentId: item[props.tableName + 'Id'] },
							}">
							{{ item["title"] }}
						</router-link>
					</span>
					<span v-if="props.tableName === 'board'">({{ item["commentCount"] }})</span>
					<NewIcon v-bind:submitDate="item['submitDate']" />
					<span v-if="props.tableName === 'board' && item['fileExistence']">
						<i class="bi bi-paperclip"></i>
					</span>
					<span v-else-if="props.tableName === 'question' && item['isLocked']">
						<i class="bi bi-lock-fill"></i>
					</span>
				</span>
				<span style="flex-basis: 8%">{{ item["viewCount"] }}</span>
				<span style="flex-basis: 19%">{{ submitDateToStr(item["submitDate"]) }}</span>
				<span style="flex-basis: 13%">{{ item["nickname"] }}</span>
			</span>
		</div>
		<div style="padding: 5px; padding-top: 10px" v-else>검색 결과가 없습니다.</div>
	</div>
</template>

<style scoped></style>

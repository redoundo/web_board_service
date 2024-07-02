<script setup>
	import router from "../../../js/router.js";
	import newIcon from "../../global/newicon.vue";
	const props = defineProps({ content: Array, total: Number });
	function goto(id) {
		router.push({ name: "notifyView", query: { contentId: id } });
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
				<span style="flex-basis: 80%; font-weight: bold">공지사항</span>
				<span style="flex-basis: 20%; font-size: 0.75rem">
					<button style="color: white" type="button" v-on:click="router.push({ name: 'notify' })">
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
				<span style="flex-basis: 15%">분류</span>
				<span style="flex-basis: 70%">제목</span>
			</span>
			<div style="width: 100%" v-if="(props.content ?? null) !== null && props.content.length > 0">
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
					v-for="(item, index) in props.content"
					v-bind:key="index">
					<span style="flex-basis: 15%; text-decoration: underline">{{ props.total - index }}</span>
					<span style="flex-basis: 15%; text-decoration: underline">{{
						item["categoryName"]
					}}</span>
					<!--        <button class="goToDetailNotificationBtn" type="button" v-on:click="goto(item['notifyId'])" v-text="item['title']"/>-->
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
								<router-link
									class="titleLink"
									v-bind:to="{ name: 'notifyView', query: { contentId: item['notifyId'] } }">
									{{ item["title"] }}
								</router-link>
								<newIcon v-bind:submitDate="item['submitDate']" />
							</span>
						</span>
					</span>
				</span>
			</div>
			<div style="padding: 5px; padding-top: 10px" v-else>공지사항 게시글이 존재하지 않습니다.</div>
		</div>
	</div>
</template>

<style scoped>
	.goToDetailNotificationBtn,
	.titleLink {
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		word-break: break-all;
	}
</style>

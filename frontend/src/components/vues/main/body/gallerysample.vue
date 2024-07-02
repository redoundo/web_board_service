<script setup>
	import router from "../../../js/router.js";
	import newIcon from "../../global/newicon.vue";
	const props = defineProps({ content: Array, total: Number });

	function goto(id) {
		router.push({ name: "galleryView", query: { contentId: id } });
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
				<span style="flex-basis: 80%; font-weight: bold">갤러리</span>
				<span style="flex-basis: 20%; font-size: 0.75rem">
					<button style="color: white" type="button" v-on:click="router.push({ name: 'gallery' })">
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
				<span style="flex-basis: 70%"></span>
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
						align-items: center;
					"
					v-for="(item, index) in props.content"
					v-bind:key="index">
					<span style="flex-basis: 15%; text-decoration: underline">{{ props.total - index }}</span>
					<span style="flex-basis: 15%; text-decoration: underline; margin-right: 1em">
						{{ item["categoryName"] }}
					</span>
					<span style="flex-basis: 70%; text-align: left; display: flex; flex-direction: row">
						<span style="flex-basis: 80%">
							<span
								style="
									text-align: left;
									display: flex;
									flex-direction: row;
									justify-content: left;
									gap: 5px;
									align-items: center;
								">
								<img
									style="width: 50px; height: 50px; flex-basis: 50%"
									v-on:click="goto(item['boardId'])"
									alt="이미지 썸네일"
									v-bind:src="
										item['files'].includes('||')
											? 'http://localhost:5000/@fs/' + item['files'].split('||')[0]
											: 'http://localhost:5000/@fs/' + item['files']
									" />
								<span style="text-decoration: underline; flex-basis: 20%; margin-left: 1em">
									+{{ item["files"].includes("||") ? item["files"].split("||").length : 1 }}
								</span>
								<newIcon v-bind:submitDate="item['submitDate']" />
							</span>
						</span>
					</span>
				</span>
			</div>
			<div style="padding: 5px; padding-top: 10px" v-else>갤러리 게시글이 존재하지 않습니다.</div>
		</div>
	</div>
</template>

<style scoped></style>

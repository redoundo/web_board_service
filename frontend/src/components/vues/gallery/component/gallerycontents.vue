<script setup>
	import NewIcon from "../../global/newicon.vue";
	const props = defineProps({ contents: Array, conditions: Map });
</script>

<template>
	<div v-if="(props.contents ?? null) !== null && props.contents.length > 0">
		<span
			style="
				padding: 5px;
				max-height: 150px;
				margin-top: 10px;
				margin-bottom: 10px;
				display: flex;
				flex-direction: row;
				border: 2px solid gainsboro;
			"
			v-for="(item, index) in contents"
			v-bind:key="index">
			<span
				style="
					min-width: 170px;
					min-height: 100px;
					display: flex;
					flex-direction: row;
					justify-content: center;
				">
				<img
					style="min-width: 100px; max-height: 150px"
					class="thumbnailImage"
					alt="갤러리 이미지"
					v-bind:src="'http://localhost:5000/@fs/' + item['files'].split('||')[0]" />
			</span>
			<div
				style="
					display: flex;
					flex-direction: column;
					justify-content: center;
					gap: 10px;
					align-items: start;
					padding: 10px;
					max-width: 350px;
					text-align: left;
				">
				<span style="font-size: 15px">
					<router-link
						v-bind:to="{
							name: 'galleryView',
							query: { ...conditions, contentId: item['boardId'] },
						}">
						{{ item["title"] }}
					</router-link>
					<!--          <button type="button" v-on:click="goto(item.get('galleryId'))">{{item.get("title")}}</button>-->
					<NewIcon v-bind:submitDate="item['submitDate']" />
				</span>
				<span
					style="
						word-break: keep-all;
						max-height: 200px;
						overflow: hidden;
						text-overflow: ellipsis;
						display: -webkit-box;
						font-size: 13px;
						-webkit-line-clamp: 3;
						-webkit-box-orient: vertical;
					">
					{{ item["content"] }}
				</span>
			</div>
		</span>
	</div>
	<div
		style="
			font-size: 11.5px;
			line-height: normal;
			margin-bottom: 5px;
			padding: 5px;
			padding-top: 10px;
		"
		v-else>
		검색 결과가 존재하지 않습니다.
	</div>
</template>

<style scoped></style>

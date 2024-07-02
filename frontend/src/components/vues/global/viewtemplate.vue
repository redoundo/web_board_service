<script setup>
	const props = defineProps({ content: Map });
	function isGallery() {
		return document.location.href.includes("gallery");
	}
</script>

<template>
	<span
		style="
			margin-bottom: 10px;
			font-size: 1.125rem;
			text-align: left;
			font-weight: bold;
			margin-left: 10px;
		"
		v-text="props.content.get('headTitle')"></span>
	<span
		style="
			border-bottom-width: 2px;
			display: flex;
			flex-direction: row;
			justify-content: space-between;
			padding-bottom: 7px;
			border-bottom: 2px solid gainsboro;
		">
		<span
			style="
				display: flex;
				flex-direction: row;
				justify-content: space-between;
				gap: 20px;
				padding: 0px 0px 0px 10px;
			">
			<span
				v-if="(props.content.get('categoryName') ?? null) !== null"
				v-text="props.content.get('categoryName')">
			</span>
			<span
				v-if="(props.content.get('title') ?? null) !== null"
				v-text="props.content.get('title')"></span>
		</span>
		<span>
			<span
				style="margin-right: 7px"
				v-if="(props.content.get('submitDate') ?? null) !== null"
				v-text="props.content.get('submitDate')"></span>
			<span
				v-if="(props.content.get('nickname') ?? null) !== null"
				v-text="props.content.get('nickname')"></span>
		</span>
	</span>
	<span v-bind:class="isGallery() ? 'isGallery' : 'isNotGallery'">
		<span v-if="(props.content.get('viewCount') ?? null) !== null">
			조회수 : <span style="margin-left: 5px"> {{ props.content.get("viewCount") }}</span>
		</span>
		<div
			v-if="isGallery() == false"
			style="
				text-wrap: wrap;
				word-break: break-all;
				border-width: 1px;
				padding: 1em;
				height: 75%;
				line-height: normal;
				text-align: left;
				border: 2px solid gainsboro;
				text-align: left;
				margin-top: 10px;
			">
			{{ props.content.get("content") }}
		</div>
	</span>
</template>

<style scoped>
	.isNotGallery {
		text-align: end;
		padding-right: 0.5rem;
		min-height: 100px;
		margin-top: 10px;
	}
	.isGallery {
		text-align: end;
		padding-right: 0.5rem;
		margin-top: 10px;
	}
</style>

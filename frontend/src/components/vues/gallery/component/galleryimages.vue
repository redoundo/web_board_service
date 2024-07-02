<script setup>
	import { onMounted } from "vue";

	const props = defineProps({ images: Array, imageIndex: Number });
	const emits = defineEmits(["changeImageIndex"]);

	onMounted(() => {
		console.log(props.images);
		console.log(props.imageIndex);
	});
</script>

<template>
	<span style="margin: 5px; display: flex; flex-direction: row; justify-content: center; gap: 10px">
		<span style="display: flex; justify-content: center; min-width: 65px">
			<button
				type="button"
				v-if="images.length > 0 && imageIndex > 0"
				v-on:click="emits('changeImageIndex', props.imageIndex - 1)">
				<i style="font-size: 50px; color: #3c3633" class="bi bi-chevron-left"></i>
			</button>
		</span>
		<div style="padding-bottom: 10px">
			<img
				style="min-width: 225px; max-height: 250px; min-height: 150px"
				class="galleryImage"
				v-bind:src="'http://localhost:5000/@fs/' + props.images.at(props.imageIndex)['imagePath']"
				alt="갤러리 이미지" />
			<span style="display: flex; flex-direction: row; justify-content: center; padding-top: 20px">
				<span style="display: flex; flex-direction: row; justify-content: space-between; gap: 7px">
					<span v-for="(item, index) in props.images" v-bind:key="index">
						<button
							type="button"
							class="imageIndexDot"
							v-on:click="emits('changeImageIndex', index)"></button>
					</span>
				</span>
			</span>
		</div>
		<span style="display: flex; justify-content: center; min-width: 65px">
			<button
				type="button"
				v-if="images.length > 0 && images.length - (imageIndex + 1) > 0"
				v-on:click="emits('changeImageIndex', props.imageIndex + 1)">
				<i style="font-size: 50px; color: #3c3633" class="bi bi-chevron-right"></i>
			</button>
		</span>
	</span>
</template>

<style scoped>
	.imageIndexDot {
		border-radius: 10px;
		background-color: #3c3633;
		width: 8px;
		height: 8px;
	}
	.galleryImage {
		width: 200px;
		height: 300px;
		overflow: hidden;
	}
</style>

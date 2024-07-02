<script setup>
	import { ref } from "vue";
	import { checkString } from "../../../utils/checkValid.js";
	const props = defineProps({ index: Number });
	const emits = defineEmits(["fileUpload", "deleteUploadedFile"]);

	const src = ref("");

	function upload(event, index) {
		const { files } = event.target;
		if (files.length < 1) return;
		if (files[0].size() > 1024 * 1024) return alert("1MB 를 초과 하는 파일은 업로드 불가능합니다.");

		emits("fileUpload", files[0], index);
		const fileReader = new FileReader();
		fileReader.readAsDataURL(files[0]);
		fileReader.onload = function (ev) {
			if (ev.loaded) src.value = ev.target.result;
		};
	}
</script>

<template>
	<span
		style="
			display: flex;
			flex-direction: row;
			justify-content: space-between;
			margin-bottom: 10px;
			margin-top: 5px;
			font-size: 12px;
		">
		<img
			style="min-width: 40px; min-height: 40px; max-width: 50px"
			v-if="checkString(src)"
			v-bind:src="src"
			alt="사용자 이미지" />
		<input
			v-on:change="upload($event, props.index)"
			v-bind:name="'file' + index.toString()"
			type="file"
			accept=".jpg,.png,.gif" />
		<button
			style="
				background-color: white;
				border-color: #3c3633;
				border-width: 1px;
				border-radius: 3px;
				width: 20px;
				height: 20px;
			"
			type="button"
			v-on:click="emits('deleteUploadedFile', props.index)">
			X
		</button>
	</span>
</template>

<style scoped></style>

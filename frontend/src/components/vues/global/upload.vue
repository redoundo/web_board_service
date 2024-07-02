<script setup>
	import { checkString } from "../../utils/checkValid";
	import { ref } from "vue";
	const props = defineProps({ index: Number, table: String });
	const emits = defineEmits(["fileUpload", "deleteUploadedFile"]);

	const src = ref("");
	/**
	 * 이미지 업로드
	 * @param {*} event
	 * @param {Number} index
	 */
	function upload(event, index) {
		const { files } = event.target;
		console.log(files);
		if (files.length < 1) return;
		if (files[0].size > 1024 * 1024 * 2)
			return alert("2MB 를 초과 하는 파일은 업로드 불가능합니다.");

		emits("fileUpload", files[0], index);
		if (props.table === "gallery") {
			const fileReader = new FileReader();
			fileReader.readAsDataURL(files[0]);
			fileReader.onload = function (ev) {
				if (ev.loaded) src.value = ev.target.result;
			};
		}
	}
</script>

<template>
	<span>
		<img
			style="min-width: 40px; min-height: 40px; max-width: 50px"
			v-if="props.table === 'gallery' && checkString(src)"
			v-bind:src="src"
			alt="사용자 이미지" />
		<input
			v-on:change="upload($event, props.index)"
			name="files"
			type="file"
			style="font-size: 11px; border: 0px; padding-right: 5px"
			accept=".jpg,.png,.gif" />
		<button
			type="button"
			v-on:click="emits('deleteUploadedFile', props.index)"
			style="
				background-color: white;
				border: 1px solid gray;
				width: 18px;
				height: 18px;
				color: dimgrey;
				padding: 0px;
			">
			x
		</button>
	</span>
</template>

<style scoped></style>

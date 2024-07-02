<script setup>
	const props = defineProps({ index: Number });
	const emits = defineEmits(["fileUpload", "deleteUploadedFile"]);

	function upload(event, index) {
		const files = event.target.value;
		if (files.length < 1) return; // 업로드 안되었을 때는 처리 안함.
		console.log(files);
		if (files.size() > 1024 * 1024 * 2)
			return alert("2MB 를 초과 하는 파일은 업로드 불가능합니다.");

		emits("fileUpload", files, index);
	}
</script>

<template>
	<span>
		<input
			v-on:change="upload($event, props.index)"
			name="files"
			type="file"
			accept=".jpg,.png,.gif" />
		<button type="button" v-on:click="emits('deleteUploadedFile', props.index)">X</button>
	</span>
</template>

<style scoped></style>

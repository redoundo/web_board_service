<script setup>
	const props = defineProps({ title: String, content: String, categoryId: Number });
	const emits = defineEmits(["setValue", "deleteUploadedFile"]);
</script>

<template>
	<span class="aDetail">
		<span class="aDetailName">분류*</span>
		<span class="aDetailValue">
			<select
				required
				style="
					font-size: 12px;
					padding-top: 3px;
					padding-bottom: 3px;
					padding-right: 135px;
					padding-left: 10px;
				"
				v-if="(categories ?? null) !== null && categories.length > 0"
				v-bind:value="category"
				v-on:change="emits('setValue', 'category', $event.target.value)">
				<option value="null" v-bind:selected="props.categoryId === null">분류 선택</option>
				<option
					v-for="(item, index) in categories"
					v-bind:selected="category === item['categoryId']"
					v-bind:key="index"
					v-bind:value="item['categoryId']">
					{{ item["categoryName"] }}
				</option>
			</select>
			<select
				style="
					font-size: 12px;
					padding-top: 3px;
					padding-bottom: 3px;
					padding-right: 135px;
					padding-left: 10px;
				"
				v-else>
				<option value="null" selected>분류 선택</option>
			</select>
		</span>
	</span>
	<span class="aDetail">
		<span class="aDetailName">제목*</span>
		<span class="aDetailValue">
			<input
				class="grow"
				v-bind:value="title"
				type="text"
				placeholder="제목을 입력해주세요"
				v-on:change="setValue('title', $event.target.value)"
				required
				maxlength="99"
				style="
					font-size: 12px;
					padding-top: 3px;
					padding-bottom: 3px;
					padding-right: 135px;
					padding-left: 10px;
				" />
		</span>
	</span>
	<span class="aDetail">
		<span class="aDetailName">내용*</span>
		<span class="aDetailValue">
			<textarea
				v-bind:value="content"
				v-on:change="setValue('content', $event.target.value)"
				required
				maxlength="4000"
				placeholder="내용을 입력해 주세요"
				style="
					font-size: 12px;
					outline: none;
					resize: none;
					height: 90px;

					padding: 5px;
				"
				class="grow"></textarea>
		</span>
	</span>
</template>

<style scoped></style>

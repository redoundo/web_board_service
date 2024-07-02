<script setup>
	import { onMounted, ref } from "vue";
	import axios from "../../js/axios/axios.js";
	import ContentResult from "../global/contentresult.vue";
	import SearchOptions from "../global/searchoptions.vue";
	import PageOptions from "../global/pageoptions.vue";
	import Pagination from "../global/pagination.vue";
	import { queryParser, mapQueryString } from "../../utils/queryParsing.js";
	import router from "../../js/router.js";

	const conditions = ref(queryParser(router.currentRoute.value.query));

	const categories = ref({});
	const total = ref(0);
	const contents = ref([]);

	onMounted(() => {
		notifyAxios();
	});

	async function notifyAxios() {
		console.log(conditions.value);
		const success = function (value) {
			console.log(value?.data);
			if ((value ?? null) !== null && (value.data ?? null) !== null) {
				contents.value = value.data["results"];
				total.value = value.data["total"];
				categories.value = value.data["categories"];
			}
		};
		const query = mapQueryString("/notify", conditions.value);
		console.log(query);
		console.log(conditions.value);
		await axios.get({ path: query, onSuccess: success, onError: null });
	}

	function changeCondition(what, how) {
		console.log(what, how);
		conditions.value.set(what, how);
		console.log(conditions.value);
	}
</script>

<template>
	<div style="display: flex; flex-direction: column; align-items: center; margin-top: 10px">
		<div style="display: flex; flex-direction: column; width: 95%; min-width: 665px">
			<span style="margin-bottom: 0.75rem; font-size: 15px; text-align: left">공지사항</span>
			<div>
				<SearchOptions
					v-bind:categories="categories"
					v-bind:conditions="conditions"
					v-on:changeCondition="changeCondition"
					v-on:emitAxios="notifyAxios" />
				<PageOptions v-on:changeCondition="changeCondition" v-bind:conditions="conditions" />
			</div>
			<ContentResult
				v-bind:total="total"
				v-bind:contents="contents"
				v-bind:conditions="conditions"
				v-bind:table-name="'notify'" />
			<Pagination
				v-bind:page="conditions.get('page')"
				v-bind:total="total"
				v-on:changeCondition="changeCondition" />
		</div>
	</div>
</template>

<style scoped></style>

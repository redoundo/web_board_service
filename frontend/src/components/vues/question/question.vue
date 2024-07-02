<script setup>
	import { onMounted, ref } from "vue";
	import axios from "../../js/axios/axios.js";
	import ContentResult from "../global/contentresult.vue";
	import QuestionOptions from "./questionoption.vue";
	import PageOptions from "../global/pageoptions.vue";
	import Pagination from "../global/pagination.vue";
	import router from "../../js/router.js";
	import { queryParser, queryString } from "../../utils/queryParsing.js";
	import { jwtParser } from "../../utils/jwtparser.js";
	import { checkString } from "../../utils/checkValid.js";

	const props = defineProps({ nickname: String, login: Boolean, user: Object });

	const inquiryHistory = ref(false);
	const conditions = ref(routeParser(router.currentRoute.value.query));

	const total = ref(0);
	const contents = ref([]);

	onMounted(() => {
		console.log(props.user);
		questionAxios();
	});

	function routeParser(currentQuery) {
		const map = queryParser(currentQuery);
		if (currentQuery !== null) {
			const queryValues = { ...currentQuery };
			if ((queryValues["inquiryHistory"] ?? null) !== null && queryValues["inquiryHistory"] === "1")
				map.set("inquiryHistory", true);
		}
		return map;
	}

	async function questionAxios() {
		const currentQuery = router.currentRoute.value.query;
		const conditions = routeParser(currentQuery);

		const success = function (value) {
			console.log(value.data);
			if ((value ?? null) !== null && (value.data ?? null) !== null) {
				contents.value = value.data["results"];
				total.value = value.data["total"];
			}
		};

		if (
			(conditions["inquiryHistory"] ?? null) !== null &&
			conditions["inquiryHistory"] === "1" &&
			props.login
		)
			await axios.get({
				path: queryString("/user/question", {
					...currentQuery,
				}),
				onSuccess: success,
				onError: null,
			});
		else
			await axios.get({
				path: queryString("/question", {
					...currentQuery,
				}),
				onError: null,
				onSuccess: success,
			});
	}

	function changeCondition(what, how) {
		conditions.value.set(what, how);
	}
</script>

<template>
	<div style="display: flex; flex-direction: column; align-items: center; margin-top: 10px">
		<div style="display: flex; flex-direction: column; width: 95%; min-width: 665px">
			<span style="margin-bottom: 0.75rem; font-size: 15px; text-align: left">문의 게시판</span>
			<div>
				<QuestionOptions
					v-on:changeCondition="changeCondition"
					v-bind:conditions="conditions"
					v-on:emitAxios="questionAxios"
					v-bind:isChecked="inquiryHistory"
					v-bind:user="props.user" />

				<PageOptions v-on:changeCondition="changeCondition" v-bind:conditions="conditions" />
			</div>
			<div>
				<ContentResult
					v-bind:total="total"
					v-bind:contents="contents"
					v-bind:conditions="conditions"
					v-bind:user="props.user"
					v-bind:table-name="'question'" />
			</div>
			<span
				style="
					display: flex;
					flex-direction: row;
					justify-content: end;
					margin-top: 5px;
					padding-right: 15px;
				">
				<button
					v-if="props.login && checkString(jwtParser.get()) && checkString(props.nickname)"
					type="button"
					style="
						padding: 5px;
						padding-left: 7px;
						padding-right: 7px;
						font-size: 12.5px;
						font-weight: bold;
					"
					v-on:click="router.push({ name: 'questionWrite', query: { ...conditions } })">
					> 글쓰러 가기 <span style="margin-left: 5px">+</span>
				</button>
			</span>
			<Pagination
				v-bind:page="conditions.get('page')"
				v-bind:total="total"
				v-on:changeCondition="changeCondition" />
		</div>
	</div>
</template>

<style scoped></style>

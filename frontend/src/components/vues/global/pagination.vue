<script setup>
	const props = defineProps({ total: Number, page: Number });
	const emits = defineEmits(["changeCondition"]);
	function range(max, page) {
		const start = (page - 1) * 10 + 1;
		const end = page * 10;
		if (max < end) {
			return new Array(max - start + 1).fill(start + 1).map((arr, index) => {
				return arr + index;
			});
		} else {
			return new Array(end - start + 1).fill(start + 1).map((arr, index) => {
				return arr + index;
			});
		}
	}
</script>

<template>
	<span
		style="
			display: flex;
			flex-direction: row;
			justify-content: center;
			margin-top: 0.75rem;
			--tw-space-x-reverse: 0;
			margin-right: calc(2.5rem * var(--tw-space-x-reverse));
			margin-left: calc(2.5rem * calc(1 - var(--tw-space-x-reverse)));
		"
		v-if="(props.total ?? null) !== null && (props.page ?? null) !== null">
		<span v-if="Math.ceil(props.total / 10) > 1 && props.page > 1">
			<button
				class="paginations"
				type="button"
				style="margin-right: 10px"
				v-on:click="emits('changeCondition', 'page', 1)">
				<<
			</button>
			<button
				class="paginations"
				style="margin-right: 0px"
				v-if="Math.ceil(props.total / 10) > 1 && props.page > 1"
				type="button"
				v-on:click="emits('changeCondition', 'page', props.page - 1)">
				<
			</button>
		</span>
		<span>
			<span v-if="props.total <= 10">
				<button class="paginations" type="button" disabled>1</button>
			</span>
			<span v-else-if="Math.ceil(props.total / 10) <= 10">
				<button
					type="button"
					class="paginations"
					v-for="index in Math.ceil(props.total / 10)"
					v-bind:key="index"
					v-on:click="emits('changeCondition', 'page', index)">
					{{ index }}
				</button>
			</span>
			<span v-else>
				<button
					type="button"
					class="paginations"
					v-for="index in range(Math.ceil(props.total / 10), props.page)"
					v-bind:key="index"
					v-on:click="emits('changeCondition', 'page', index)">
					{{ index }}
				</button>
			</span>
		</span>
		<span v-if="Math.ceil(props.total / 10) > props.page">
			<button
				type="button"
				class="paginations"
				v-if="Math.ceil(props.total / 10) > props.page"
				v-on:click="emits('changeCondition', 'page', props.page + 1)">
				>
			</button>
			<button
				type="button"
				class="paginations"
				style="margin-right: 0px"
				v-on:click="emits('changeCondition', 'page', Math.ceil(props.total / 10))">
				>>
			</button>
		</span>
	</span>
	<span v-else>
		<button class="paginations" type="button" disabled>1</button>
	</span>
</template>

<style scoped>
	.paginations {
		font-size: 11px;
		margin-right: 10px;
	}
</style>

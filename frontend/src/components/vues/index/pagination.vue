<script setup>
const props = defineProps({total : Number , page : Number})
function range(max , page){
  const start = (page-1) *10 +1;
  const end = page*10;
  if(max < end){
    return new Array(max - start +1).fill(start + 1).map((arr , index)=>{return arr + index});
  } else {
    return new Array(end - start +1).fill(start + 1).map((arr , index)=>{return arr + index});
  }
}
</script>

<template>
  <span v-if="(props.total??null) !== null && (props.page??null) !== null">
    <span v-if="Math.ceil(props.total / 10) > 1 && props.page > 1">
      <button type="button" v-on:click="this.$emit('changeCondition' , 'page' , 1)">맨 처음으로</button>
    </span>
    <span v-if="Math.ceil(props.total / 10) > 1 && props.page > 1">
      <button type="button" v-on:click="this.$emit('changeCondition' , 'page' , props.page -1)">앞으로</button>
    </span>
    <span>
      <span v-if="props.total <= 10">
         <button type="button" disabled>1</button>
      </span>
      <span v-else-if="Math.ceil(props.total / 10) <= 10">
        <button type="button" v-for="(index) in Math.ceil(props.total / 10)" v-bind:key="index"
                v-on:click="this.$emit('changeCondition' , 'page' , index)">{{index}}</button>
      </span>
      <span v-else>
        <button type="button" v-for="index in range(Math.ceil(props.total /10) , props.page)" v-bind:key="index"
                v-on:click="this.$emit('changeCondition' , 'page' , index)">{{index}}</button>
      </span>
    </span>
    <span v-if="Math.ceil(props.total /10 ) > props.page">
      <button type="button" v-on:click="this.$emit('changeCondition'  , 'page', props.page + 1)">뒤로</button>
    </span>
    <span v-if="Math.ceil(props.total /10 ) > props.page">
      <button type="button" v-on:click="this.$emit('changeCondition' , 'page' , Math.ceil(props.total / 10))">맨 끝으로</button>
    </span>
  </span>
  <span v-else>
    <button type="button" disabled>1</button>
  </span>
</template>

<style scoped>

</style>
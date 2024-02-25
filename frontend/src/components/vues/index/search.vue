<script setup>

import {watch} from "vue";

const props = defineProps({
  conditions : Map , categories: Array
})

watch(()=>props.categories , ()=>{console.log(props.categories)});
</script>
<template>
  <div>
    <div>
    <span>
      <span>
        <label for="startDate">등록일</label>
        <input id="startDate" type="date" name="start" v-bind:value="props.conditions.get('start')"
               v-on:change="$emit('changeCondition' , 'start' , $event.target.value)"/>
      </span>
      <span>
        <label for="endDate">~</label>
        <input id="endDate" type="date" name="end" v-bind:value="props.conditions.get('end')"
               v-on:change="$emit('changeCondition' , 'end' , $event.target.value)"/>
      </span>
      <span>
        <select name="contentCategoryId" v-if="props.categories != null"
                v-on:change="$emit('changeCondition' , 'contentCategoryId' , $event.target.value)">
          <option value=null>전체 카테고리</option>
          <option v-for="(item , index) in props.categories" v-bind:key="index"
                  v-bind:value="item['categoryId']" v-text="item['categoryName']"></option>
        </select>
        <select v-else>
          <option value=null>전체 카테고리</option>
        </select>
      </span>
      <span>
        <label>
          <input type="search" name="keyword"
                 v-on:focusout="$emit('changeCondition' , 'keyword' , $event.target.value)"
                 placeholder="검색어를 입력해주세요.(제목+작성자+내용)"/>
        </label>
      </span>
    </span>
      <button type="button" v-on:click="() => {$emit('indexAxios') }">검색</button>
    </div>
  </div>
</template>

<style scoped>

</style>
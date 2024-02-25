<script setup>
import {toMap} from "/src/components/utils/arrayToMap.js";
import router from "../../js/router.js";
const props = defineProps({
  total : Number , contents : Array , conditions : Map , categories: Array
});


function goView(contentId){
  let addContentId = {...props.conditions , contentId : contentId}
  return router.push({name:'view' , query : addContentId});
}
</script>

<template>
  <div>
    <div>
      <p>총 {{props.total}}건</p>
    </div>
    <span>
      <span>카테고리</span>
      <span>파일</span>
      <span>제목</span>
      <span>작성자</span>
      <span>조회수</span>
      <span>등록일자</span>
      <span>수정일자</span>
    </span>
    <div v-if="(props.contents??null) !== null && (toMap(props.categories)??null) !== null">
      <span v-for="(item , index) in props.contents" v-bind:key="index">
      <span v-text="toMap(props.categories).get(item['contentCategoryId'])"></span>
      <span v-text="item['fileExistence']? '존재' : ''"></span>
      <span><button type="button" v-text="item['title']" v-on:click="goView(item['contentId'])"></button></span>
      <span v-text="item['nickname']"></span>
      <span v-text="item['viewCount']"></span>
      <span v-text="item['submitDate']"></span>
      <span v-text="item['updateDate'] != null ? item['updateDate'] : '-'"></span>
    </span>
    </div>
    <div v-else>
      <p>검색 결과가 존재하지 않습니다.</p>
    </div>
  </div>
</template>

<style scoped>

</style>
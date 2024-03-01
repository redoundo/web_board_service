<script setup>
import {onBeforeMount , ref, watch} from 'vue';
import axios from "axios";
import {dateThatCanChangeYear} from "/src/components/utils/dateFunction.js";
import search from "./search.vue";
import results from "./results.vue";
import pagination from './pagination.vue';
import qs from 'qs';
import {refValueToMap} from "../../utils/arrayToMap.js";
const initCondition = new Map();

const conditions = ref(initCondition);

onBeforeMount(() => {
  initCondition.set("contentCategoryId" , null);
  initCondition.set("keyword" , null);
  initCondition.set("start" , dateThatCanChangeYear(-1));
  initCondition.set("end" , dateThatCanChangeYear(null));
  initCondition.set("page" , 1);
  indexAxios()
})

let total = ref(0);
let categories = ref([]);
let contents = ref([]);

/**
 * 쿼리스트링 설정을 통해 검색 결과를 받아온다.
 * @returns {Promise<void>}
 */
async function indexAxios(){
  console.log(conditions.value , refValueToMap(conditions.value));
  const queryString = qs.stringify(refValueToMap(conditions.value) , {arrayFormat: 'repeat' , skipNulls: true})
  let url = "http://localhost:8080";
  url = queryString.length > 1 ? url + "?" + queryString : url;
  console.log("indexAxios const url = "+url);
  await axios.get(url)
      .then(value => {
        console.log(value.data)
        if((value.data??null) != null && value.data?.total !== null
            && value.data?.categories !== null && value.data?.contents !== null){
          total.value = (value.data.total);
          categories.value = Array.from(value.data.categories);
          contents.value = Array.from(value.data.contents);
        }
      })
      .catch(err => {console.log(err);})
}

watch(()=>conditions.value.get('page'),async () => {
       await indexAxios();//페이지가 바뀌면 바뀐 내용을 가지고 다시 가져오게끔 설정.
    }
)
watch(()=>categories , ()=>{console.log(categories)})

/**
 * 검색 조건 변경.
 * @param {String} what
 * @param {any} how
 */
function changeCondition(what , how){
  console.log(what , how);
  conditions.value.set(what , how);
  console.log(conditions.value)
}
</script>
<template>
  <div id="Board">
    <h2>자유 게시판 - 목록입니다.</h2>
    <div>
      <search v-on:indexAxios="indexAxios" v-on:changeCondition="changeCondition"
              v-bind:categories="categories" v-bind:conditions="conditions" />
    </div>
    <div>
      <results v-bind:total="total"  v-bind:contents="contents" v-bind:conditions="conditions"
               v-bind:categories="categories"/>
    </div>
    <div>
      <pagination v-bind:total="total" v-bind:page="conditions.get('page')"
                  v-on:changeCondition="changeCondition"/>
    </div>
    <router-link v-bind:to="{ name : 'write' , query : conditions}" v-bind:key="$router.fullPath">등록</router-link>
  </div>
</template>

<style scoped>

</style>
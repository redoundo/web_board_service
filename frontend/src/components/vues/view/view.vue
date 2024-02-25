<script setup>
import axios from "axios";
import {onBeforeMount, ref} from 'vue';
import comment from './comment.vue';
import Files from './files.vue';
import Delete from '/src/components/vues/delete/delete.vue';
import {dateThatCanChangeYear} from "/src/components/utils/dateFunction.js";
import router from "../../js/router.js";

onBeforeMount(()=>{viewAxios()})

let content = ref({});
let categoryName = ref(null);
let files = ref([]);
let comments = ref([]);
let queries = ref({});
let show = ref(false);

async function viewAxios(){
  console.log(router.currentRoute.value)
  const params = router.currentRoute.value.query;
  console.log(params["contentId"])
  if(params["contentId"] !== null){
    await axios.get("http://localhost:8080/view?contentId=" + params["contentId"])
        .then(value => {
          console.log(value.data);
          if(value.data !== null && value.data !== undefined){
            content.value = value.data.contents;
            categoryName.value = value.data.categoryName;
            files.value = value.data.files;
            comments.value = value.data.comments;
            queries.value = {...params};
          }
        })
  }
}

function goWhere(where){
  let param = {...queries};
  if(where === "index"){
    param.delete("contentId")
  }
   router.push({name : where , query : param})
}

function addComment(user , comment){
  let map = new Map();
  map.set("commentUser" , user);
  map.set("comment" , comment);
  map.set("commentedDate" , dateThatCanChangeYear(null));
  comments.value.push(map);
}

function openDeleteLayer(){
  show.value = !show.value;
}
</script>

<template>
  <div id="viewBoard">
    <h6>게시판 - 보기</h6>
    <span>
    <span v-text="content['nickname']"></span>
    <span>
      <span v-text="content['submitDate']"></span>
      <span v-text="content['updateDate']"></span>
    </span>
  </span>
    <span>
    <span>[{{categoryName}}]</span>
    <span v-text="content['title']"></span>
  </span>
    <div>
      <p v-text="content['content']"></p>
    </div>
    <div>
      <Files v-bind:files="files"/>
    </div>
    <div>
      <comment v-bind:comments="comments" v-on:addComment="addComment"/>
    </div>
    <Delete v-bind:show="show" v-bind:contentId="queries['contentId']" v-bind:password="queries['password']"
            v-on:openDeleteLayer="openDeleteLayer"/>
    <span>
    <button type="button" v-on:click="()=>goWhere('index')">목록</button>
    <button type="button" v-on:click="()=>goWhere('modify')">수정</button>
    <button type="button" v-on:click="openDeleteLayer">삭제</button>
  </span>
  </div>
</template>

<style scoped>

</style>
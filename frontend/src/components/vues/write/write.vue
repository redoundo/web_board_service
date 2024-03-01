<script setup>
import {onBeforeMount, ref} from 'vue';
import axios from "axios";
import router from '/src/components/js/router.js';
let categories = ref([]);

onBeforeMount(() => writeAxios())
const url = "http://localhost:8080";
/**
 * write.vue 를 처음 로드했을 때 존재하는 카테고리들을 가져온다.
 * @returns {Promise<void>}
 */
async function writeAxios(){
  await axios.get(url + "/write" )
      .then(value => {
        console.log(value.data);
        categories.value = value.data;
      })
      .catch(err => console.log(err))
}

/**
 * 사용자가 입력한 두 비밀번호가 동일한지 확인한 뒤에 , 입력된 내용을 보낸다.
 * @returns {Promise<void>}
 */
async function writeInsertAxios(){
  const set = document.getElementById("setPassword").value
  const checked = document.getElementById("checkPassword").value;
  console.log(set , checked)
  if(set === checked){

    let formData = new FormData();
    formData.set("contentCategoryId" , document.getElementById("writeCategoryId").value);
    formData.set("nickname" ,document.getElementById("writeNickname").value);
    formData.set("password" , document.getElementById("setPassword").value);
    formData.set("title" , document.getElementById("writeTitle").value);
    formData.set("content" , document.getElementById("writeContent").value);
    formData.set("file1" , document.getElementById("writeFile1").files[0]);
    formData.set("file2" , document.getElementById("writeFile2").files[0]);
    formData.set("file3" , document.getElementById("writeFile3").files[0]);
    console.log(formData.get("title"))
    await axios.post(url + "/write/insert" , formData , {headers : {"Content-Type" : 'multipart/form-data'}})
        .then(value => {
          console.log(value.data);
          if(value.data !== null && value.data !== undefined){
            const params = this.$route.query;//vue query 의 쿼리 값을 그대로 넣어 보낸다.
            router.push({name : "view" , query:{...params , contentId: value.data}});
          }
        })
        .catch(err=>{console.log(err)})
  } else{
    alert("비밀번호가 일치하지 않습니다.다시 입력해주세요.")
  }
}
</script>

<template>
  <div id="writeBoard">
    <form id="writeForm" enctype="multipart/form-data" >
      <div>
      <span>
        <span>카테고리*</span>
        <span>
          <select id="writeCategoryId" name="contentCategoryId" v-if="categories != null && categories.length > 0" required>
            <option value=null>카테고리 선택</option>
            <option v-for="(item , index) in categories" v-bind:key="index" v-bind:value="item['categoryId']">
              {{item['categoryName']}}
            </option>
          </select>
          <select v-else>
            <option value=null>카테고리 선택</option>
          </select>
        </span>
      </span>
        <span>
        <span>작성자*</span>
        <span><input type="text" id="writeNickname" name="nickname" required/></span>
      </span>
        <span>
        <span>비밀번호*</span>
        <span>
          <input id="setPassword" name="password" type="password" required placeholder="비밀번호"/>
          <input id="checkPassword" type="password" required placeholder="비밀번호 확인"/>
        </span>
      </span>
        <span>
        <span>제목*</span>
        <input id="writeTitle" type="text" required name="title"/>
      </span>
        <span>
        <span>내용*</span>
        <input id="writeContent" type="text" required name="content"/>
      </span>
        <span>
        <span>파일첨부</span>
        <input id="writeFile1" name="file1" type="file"/>
        <input id="writeFile2" name="file2" type="file"/>
        <input id="writeFile3" name="file3" type="file"/>
      </span>
      </div>
    </form>
    <span>
    <router-link v-bind:to="{name : 'index' , query : $router.query}">취소</router-link>
    <input form="writeForm" formenctype="multipart/form-data" type="button" v-on:click.self.prevent="writeInsertAxios" value="저장"/>
  </span>
  </div>
</template>

<style scoped>

</style>
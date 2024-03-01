<script setup>
import {onMounted, ref} from 'vue';
import axios from "axios";
import Files from './files.vue';
import router from "../../js/router.js";

onMounted(modifyAxios);
const url = "http://localhost:8080";
let query = ref({});
let categoryName = ref(null);
let content = ref({});
let files = ref([]);

async function modifyAxios(){
  const params = router.currentRoute.value.query;
  query = {...params};
  await axios.get(url + "/view/modify?contentId=" + params['contentId'])
      .then(value => {
        console.log(value.data);
        if((value.data??null) !== null && (value.data['categoryName']??null) !== null){
          categoryName.value = value.data['categoryName'];
          content.value = value.data['content'];
          files.value = value.data['files']
        }
      })
}

/**
 * 로컬에서 먼저 파일 내용을 삭제한 뒤에 남은 파일들만을 가지고 업데이트를 진행하면서 db 에 반영시킨다.
 * @param {Number} index 삭제할 파일의 위치
 * @param {String} name 삭제할 파일의 이름
 */
function deleteFile(index , name){
  if((index??null) !== null && (name??null) !== null){
    if(files[index]['fileName'] === name){
      files.value.slice(index , 1);
    }
  }
}

/**
 * 파일은 사용자가 업로드할 multiPartFile 과 db 에서 가져온 파일 정보로 나눠진다.
 * 파일 정보는 notFile1/2/3 이라는 이름으로
 * multiPartFile 은 file1/2/3 이라는 이름으로 내용을 받아온다.
 * @returns {Promise<void>}
 */
async function updateAxios(){
  if((query['contentId']??null) !== null){
    let formData = new FormData();
    formData.set("nickname" , document.getElementById("modifyNickname").getAttribute("value"))
    formData.set("password" , document.getElementById("modifyPassword").getAttribute("value"))
    formData.set("title" , document.getElementById("modifyTitle").getAttribute("value"))
    formData.set("content" , document.getElementById("modifyContent").getAttribute("value"))
    if(files.value.length > 0){
      for(let a=0;a < files.value.length ; a++){
        formData.set("notFile" + a , files[a]);
      }
    }
    formData.set("file1" , document.getElementById("modifyFile1")?.getAttribute("value"));
    formData.set("file2" , document.getElementById("modifyFile2")?.getAttribute("value"));
    formData.set("file3" , document.getElementById("modifyFile3")?.getAttribute("value"));

    await axios.post(url + "/view/modify/update?contentId=" + query["contentId"] , formData)
        .then(value => {
          console.log(value.data);
          this.$router.push({name : "view" , query : query});
        })
        .catch(err=>{console.log(err) ;alert(err)})
  }
}

</script>

<template>
  <div id="modifyBoard">
    <h5>게시판 - 수정</h5>
    <h6>기본 정보</h6>
    <div>
    <span>
      <span>카테고리</span>
      <span v-text="categoryName"></span>
    </span>
      <span>
      <span>등록일시</span>
      <span v-text="content['submitDate']"></span>
    </span>
      <span>
      <span>수정일시</span>
      <span v-text="content['updateDate']"></span>
    </span>
      <span>
      <span>조회수</span>
      <span v-text="content['viewCount']"></span>
    </span>
      <div id="modifyForm">
    <span>
      <span>작성자*</span>
      <span><input id="modifyNickname" type="text" name="nickname" v-bind:value="content['nickname']" required/></span>
    </span>
        <span>
      <span>비밀번호*</span>
      <span><input id="modifyPassword" type="password" name="password" required placeholder="비밀번호"/></span>
    </span>
        <span>
      <span>제목*</span>
      <span><input required id="modifyTitle" type="text" name="title" v-bind:value="content['title']"/></span>
    </span>
        <span>
      <span>내용*</span>
      <span><input type="text" id="modifyContent" name="content" v-bind:value="content['content']"/></span>
    </span>
        <div>
          <Files v-bind:files="files" v-on:deleteFile="deleteFile"/>
        </div>
      </div>
    </div>
    <div>
    <span>
      <button type="button" v-on:click="this.$router.push({name:'view' , query : query})">취소</button>
      <input type="button" v-on:click="()=> updateAxios()"/>
    </span>
    </div>
  </div>
</template>

<style scoped>

</style>
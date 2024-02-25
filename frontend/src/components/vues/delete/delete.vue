<script setup>
import axios from "axios";

const props = defineProps({show : Boolean , contentId : Number , original : String})

/**
 * 사용자가 입력한 비밀번호와 프론트로 가져와진 비밀번호가 동일한지 확인한다.
 * 동일하다면 다시 axios 를 보내 db 에서 가져온 비밀번호와 사용자가 입력한 비밀번호를 비교한다. 동일하다면 현재 내용을 삭제한다.
 * @returns {Promise<void>}
 */
async function checkAndDelete(){
  const password = document.getElementById("deletePassword").getAttribute("value");
  if((password??null) !== null){
    await axios.post("http://localhost:8080/view/delete/checkPassword" , {password : password , original : props.original})
        .then(value => {
          console.log(value.data);
          if((value.data??null) !== null && value.data){
            axios.post("http://localhost:8080/view/delete" , {password : password , contentId :props.contentId})
                .then(value1 => {
                  console.log(value1.data);
                })
                .catch(err1 => {
                  console.log(err1);
                  alert(err1);
                })
          } else{
            alert("비밀번호가 맞지 않습니다. 다시 시도해주세요.")
          }
        })
        .catch(err=>{console.log(err);alert(err)})
  }
}

</script>

<template>
  <div v-bind:style="{display : props.show}">
  <span>
    <span>비밀번호*</span>
    <input type="password" required id="deletePassword"/>
  </span>
    <span>
    <button type="button" v-on:click="$emit('openDeleteLayer')">취소</button>
    <button type="button" v-on:click="checkAndDelete">확인</button>
  </span>
  </div>
</template>

<style scoped>

</style>
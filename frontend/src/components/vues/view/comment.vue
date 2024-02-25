<script setup>
import axios from "axios";

const props = defineProps({
  comments : Array
})

async function commentAxios(){
  const user = document.getElementById("commentUser").getAttribute("value");
  const comment = document.getElementById("comment").getAttribute("value");
  if((user??null) !== null && (comment??null) !== null){

    let formData = new FormData();
    formData.set("commentUser" , user);
    formData.set("comment" , comment);

    await axios.post("http://localhost:8080/view/comment" , formData)
        .then(value => {
          console.log(value.data);
          this.$emit("addComment" , user , comment);
        })
        .catch(err => {console.log(err)})
  }else{
    alert("댓글 내용과 댓글 작성자 정보가 필요합니다.")
  }
}

</script>

<template>
  <div>
    <div id="commentsDiv" v-if="(props.comments??null) !== null && props.comments.length > 0">
      <div v-for="(item , index) in props.comments" v-bind:key="index">
        <p v-text="item['commentUser']"></p>
        <p v-text="item['commentedDate']"></p>
        <p v-text="item['comment']"></p>
      </div>
    </div>
    <div>
      <form enctype="multipart/form-data">
        <input id="commentUser" type="text" name="commentUser"/>
        <input id="comment" type="text" name="comment" placeholder="댓글을 입력해주세요."/>
        <input type="submit" v-on:submit="()=>{commentAxios();return false}" value="등록"/>
      </form>
    </div>
  </div>
</template>
<style scoped>

</style>
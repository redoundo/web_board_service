<script setup>
import axios from "axios";

const props = defineProps({files : Array})

/**
 * db 에서 가져온 파일 정보에서 알아낸 파일 이름과 파일 경로를 통해 다운로드한다.
 * @param {String} name
 * @param {String} path
 * @returns {Promise<void>}
 */
async function downloadAxios(name ,path){
  const map = {"filePath" : path , "fileName" : name}
  await axios.post("http://localhost:8080/view/modify/download" , map)
      .then(value => {
        console.log(value.data)
      })
      .catch(err=> {
        console.log(err)
      })
}

</script>

<template>
  <div v-if="(props.files??null) !== null && props.files.length > 0">
        <span v-for="(item , index) in props.files" v-bind:key="index">
          <span>존재</span>
          <span v-text="item['fileName']"></span>
          <button type="button" v-on:click="()=>downloadAxios(item['fileName'] , item['filePath'])">Download</button>
          <button type="button" v-on:click="this.$emit('deleteFile' , index , item['fileName'])">X</button>
        </span>
    <div v-if="props.files.length < 3">
      <input v-for="num in (3-props.files.length)"  v-bind:id="'modifyFile' + num"
             v-bind:key="num" v-bind:name="'file'+num" type="file"/>
    </div>
  </div>
</template>

<style scoped>

</style>
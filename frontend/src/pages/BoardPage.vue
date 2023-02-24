<template>
  <q-page class="q-pa-md flex flex-center">
    <div class="q-pa-md">
      <div class="row text-h3 text-bold">
        Retro board session
      </div>
      <div class="row justify-center">
        <q-btn size="22px"
               class="q-px-xl q-py-xs" color="primary" label="Create" style="width: 50%;"/>
      </div>
      <div class="row justify-center">
        <q-btn size="22px"
               class="q-px-xl q-py-xs" color="secondary" label="Join" style="width: 50%;"/>
      </div>
    </div>
  </q-page>
</template>

<script>
import {defineComponent} from 'vue'

export default defineComponent({
  name: 'BoardPage'
})
</script>

<style lang="sass" scoped>
.row > div
  padding: 45px 45px
  background: rgba(86,61,124,.15)
  border: 1px solid rgba(86,61,124,.2)
.row + .row
  margin-top: 3rem
</style>

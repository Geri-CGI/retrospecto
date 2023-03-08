<template>
  <router-view />
</template>

<script>
import {defineComponent} from 'vue'
import {stompClientStore} from 'stores/stomp'

export default defineComponent({
  name: 'App',
  setup() {
    const store = stompClientStore()
    store.connect()
  }
})
</script>

<template>
  <q-page class="flex flex-center">
    <div class="q-pa-md">
      <q-card class="my-card">
        <q-card-section class="bg-primary text-white">
          <div class="text-h6">RetroBoard</div>
          <div class="text-subtitle2">Session</div>
        </q-card-section>

        <q-separator />

        <q-card-actions vertical>
          <q-btn flat>Create</q-btn>
          <q-btn flat>Join</q-btn>
        </q-card-actions>
      </q-card>
    </div>
  </q-page>
</template>

<script>
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'IndexPage'
})
</script>

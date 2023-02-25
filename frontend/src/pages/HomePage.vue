<template>
  <div class="q-mx-auto">
    <div class="row">
      <div class="col text-center">
        <q-parallax :height="400" src="~assets/stock.jpg">
          <div class="col text-center">
            <h1 class="text-white text-bold">retrospecto</h1>
            <h3 class="text-white">The best open source retrospective tool</h3>
          </div>
        </q-parallax>
      </div>
    </div>
    <div class="row">
      <div class="col full-height">
        <q-card class="my-card full-height" flat square>
          <div class="text-center">
            <q-icon name="dashboard" color="blue" size="6.4em" style="padding: 30px"/>
          </div>

          <q-card-section>
            <div class="text-h3 text-center" style="padding-bottom: 20px">Board</div>
            <div class="text-subtitle2 text-center">A retrospective (from Latin retrospectare, "look back"), generally,
              is a look back at events that took place, or works that were produced, in the past. We provide you a board
              to help facilitate it.
            </div>
          </q-card-section>

          <q-card-section class="q-pt-none">
            {{ lorem }}
          </q-card-section>
        </q-card>
      </div>
      <div class="col">
        <q-card class="my-card full-height" flat square>
          <div class="text-center">
            <q-icon name="style" color="blue" size="6.4em" style="padding: 30px"/>
          </div>

          <q-card-section>
            <div class="text-h3 text-center" style="padding-bottom: 20px">Poker</div>
            <div class="text-subtitle2 text-center">In planning poker, members of the group make estimates by playing
              numbered cards face-down to the table, instead of speaking them aloud.
            </div>
          </q-card-section>

          <q-card-section class="q-pt-none">
            {{ lorem }}
          </q-card-section>
        </q-card>
      </div>
      <div class="col">
        <q-card class="my-card full-height" flat square>
          <div class="text-center">
            <q-icon name="chat" color="blue" size="6.4em" style="padding: 30px"/>
          </div>

          <q-card-section>
            <div class="text-h3 text-center" style="padding-bottom: 20px">Chat</div>
            <div class="text-subtitle2 text-center">Just a simple chat feature for websocket testing with Spring Boot.
              Come and have a talk with anyone. We do not store any history of the chat.
            </div>
          </q-card-section>

          <q-card-section class="q-pt-none">
            {{ lorem }}
          </q-card-section>
        </q-card>
      </div>
    </div>
    <div class="row">
      <div class="col">
        <q-parallax :height="400" src="~assets/stock.jpg">
          <div class="col text-center vertical-middle">
            <h1 class="text-white text-bold">contribute</h1>
            <a href="https://github.com/Geri-CGI/retrospecto" target="_blank"><h3 class="text-white">
              https://github.com/Geri-CGI/retrospecto</h3></a>
          </div>
        </q-parallax>
      </div>
    </div>
    <div class="row">
      <div class="col full-height">
      </div>
      <div class="col full-height">
        <q-card class="my-card full-height" flat square>
          <div class="text-center">
            <q-icon name="help" color="blue" size="6.4em" style="padding: 30px"/>
          </div>

          <q-card-section>
            <div class="text-h3 text-center" style="padding-bottom: 20px">Good to know</div>
            <div class="text-subtitle2 text-center">Retrospecto is not storing any data on a long term. The board, poker
              and chat session are only stored in memory until the session is open by at least one client. We are not
              using any database, so your data is safe.
            </div>
          </q-card-section>


        </q-card>
      </div>
      <div class="col full-height">
      </div>
    </div>
  </div>
</template>

<script>
import {defineComponent} from 'vue'

export default defineComponent({
  name: 'HomePage'
})
</script>

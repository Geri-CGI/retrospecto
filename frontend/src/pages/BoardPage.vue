<template>
  <q-page class="q-pa-md">
    <q-dialog v-model="alert">
      <q-card>
        <q-card-section>
          <div class="text-h6">Edit the message:</div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <q-input v-model="alertMessage.cardMessage" type="textarea">
          </q-input>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn v-close-popup color="primary" flat label="Ok" @click="sendEditMessage"/>
          <q-btn v-close-popup color="negative" flat label="Close"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
    <q-dialog v-model="deleteAlert">
      <q-card>
        <q-card-section>
          <div class="text-h6">Are you sure you want to delete this card?</div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          {{ deleteAlertMessage.cardMessage }}
        </q-card-section>
        <q-card-actions align="right">
          <q-btn v-close-popup color="primary" flat label="Ok"
                 @click="deleteCard(deleteAlertMessage, deleteAlertIndex)"/>
          <q-btn v-close-popup color="negative" flat label="Close"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
    <q-dialog v-model="actionAlert">
      <q-card>
        <q-card-section>
          <div class="text-h6">Action:</div>
        </q-card-section>
        <q-card-section class="q-pt-none">
          <q-input v-model="actionAlertMessage.actionMessage" type="textarea">
          </q-input>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn v-close-popup color="primary" flat label="Ok"
                 @click="addAction(actionAlertMessage, actionAlertIndex)"/>
          <q-btn v-close-popup color="negative" flat label="Close"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
    <q-dialog v-model="createBoardAlert">
      <q-card>
        <q-img src="~assets/retrospecto-board.png" width="455px">
          <div class="absolute-bottom text-subtitle1 text-left bg-transparent text-black">
            Column names:
          </div>
        </q-img>
        <q-card-section>
          <q-form class="q-gutter-md text-center" @submit="onSubmit">
            <q-input v-model="retroBoardInput.firstColumnName"
                     :rules="[ val => val && val.length > 0 || 'Please type something']" dense
                     filled
                     hint="First column name:" lazy-rules/>
            <q-input v-model="retroBoardInput.secondColumnName"
                     :rules="[ val => val && val.length > 0 || 'Please type something']" dense
                     filled
                     hint="Second column name:" lazy-rules/>
            <q-input v-model="retroBoardInput.thirdColumnName"
                     :rules="[ val => val && val.length > 0 || 'Please type something']" dense
                     filled
                     hint="Third column name:" lazy-rules/>
            <q-input v-model="retroBoardInput.fourthColumnName"
                     :rules="[ val => val && val.length > 0 || 'Please type something']" dense
                     filled
                     hint="Fourth column name:" lazy-rules/>
            <div>
              <q-btn color="primary" label="Create" type="submit"/>
            </div>
          </q-form>
        </q-card-section>
      </q-card>

    </q-dialog>
    <div class="column justify-center items-center content-center">
      <div class="row justify-center items-center content-center">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 ">
          <div v-if="spinnerVisible"
               class="row justify-center" style="padding: 30px">
            <q-spinner
              :thickness="10"
              color="primary"
              size="5em"
            />
          </div>
          <div v-if="noWebsocketConnectionVisible" class="row justify-center" style="padding: 30px">
            <div class="text-h2">No websocket connection reload page!</div>
          </div>
        </div>
      </div>
      <div class="row">
        <div v-if="createCardVisible" class="col-xs-12 col-sm-6 col-md-6 col-lg-6 col-xl-6"
             style="padding: 30px">
          <q-card style="max-width: 300px">
            <q-card-section>
              <div class="text-h5">Create a new session</div>
              <div class="text-subtitle3">Open sessions: {{ numberOfActiveRetroBoards }}</div>
            </q-card-section>
            <q-card-actions>
              <div class="row justify-center">
                <q-input v-model="author" :error="createUsernameValid" bg-color="white"
                         error-message="Username required!"
                         label="Username" style="padding: 30px" type="text" @keydown.enter="createBoard"/>
                <q-btn color="primary" label="Create" @click="createBoard"/>
              </div>
            </q-card-actions>
          </q-card>
        </div>
        <div v-if="joinCardVisible" class="col-xs-12 col-sm-6 col-md-6 col-lg-6 col-xl-6"
             style="padding: 30px">
          <q-card style="max-width: 300px; min-width: 250px">
            <q-card-section>
              <div class="text-h5">Join a session</div>
            </q-card-section>
            <q-card-actions class="justify-center">
              <div class="justify-center">
                <div class="col-12" style="padding: 5px">
                  <q-input v-model="username" :error="joinUsernameValid" :error-message="usernameErrorMessage"
                           bg-color="white" label="Username"
                           type="text"/>
                </div>
                <div class="col-12" style="padding: 5px">
                  <q-input v-model="boardId" :error="boardIdValid" :error-message="boardIdErrorMessage" bg-color="white"
                           label="Board ID"
                           type="text"/>
                </div>
                <div class="col-12 text-center" style="p3adding: 10px">
                  <q-btn color="secondary" label="Join" @click="joinBoard"/>
                </div>
              </div>
            </q-card-actions>
          </q-card>
        </div>
      </div>
    </div>
    <div id="boardPage" class="row justify-center">
      <div v-if="messageInputVisible" class="row">
        <q-bar class="text-blue col-12" dark data-html2canvas-ignore="true">
          <div class="col text-left text-weight-bold">
            Host: {{ retroBoard.author }}
            <q-list>
              <template v-for="(user, index) in retroBoard.users" :key="index">
                <q-avatar color="primary" size="lg" text-color="white">
                  {{ getFirstLetter(user) }}
                  <q-tooltip>
                    {{ user }}
                  </q-tooltip>
                </q-avatar>
              </template>
            </q-list>
          </div>
          <div class="col text-left text-weight-bold">
            Board ID: {{ boardId }}
          </div>
          <q-btn-dropdown v-if="userIsAuthor()" color="primary" label="Menu">
            <q-list>
              <q-item v-close-popup clickable @click="orderByLikes">
                <q-item-section>
                  <q-item-label>Order by likes</q-item-label>
                </q-item-section>
                <q-item-section avatar>
                  <q-avatar color="secondary" icon="thumb_up" size="sm" text-color="white"/>
                </q-item-section>
              </q-item>
              <q-item v-close-popup clickable @click="orderByDislikes">
                <q-item-section>
                  <q-item-label>Order by dislikes</q-item-label>
                </q-item-section>
                <q-item-section avatar>
                  <q-avatar color="red-13" icon="thumb_down" size="sm" text-color="white"/>
                </q-item-section>
              </q-item>
              <q-item v-if="!retroBoard.blurActive" v-close-popup clickable @click="blurCardText()">
                <q-item-section>
                  <q-item-label>Blur text</q-item-label>
                </q-item-section>
                <q-item-section avatar>
                  <q-avatar color="primary" icon="visibility_off" size="sm" text-color="white"/>
                </q-item-section>
              </q-item>
              <q-item v-if="retroBoard.blurActive" v-close-popup clickable @click="unblurCardText()">
                <q-item-section>
                  <q-item-label>Unblur text</q-item-label>
                </q-item-section>
                <q-item-section avatar>
                  <q-avatar color="primary" icon="visibility" size="sm" text-color="white"/>
                </q-item-section>
              </q-item>
              <q-item v-close-popup clickable @click="shareTheBoard()">
                <q-item-section>
                  <q-item-label>Share the board</q-item-label>
                </q-item-section>
                <q-item-section avatar>
                  <q-avatar color="primary" icon="ios_share" size="sm" text-color="white"/>
                </q-item-section>
              </q-item>
              <q-item v-if="!retroBoard.locked" v-close-popup clickable @click="lockTheBoard">
                <q-item-section>
                  <q-item-label>Lock the board</q-item-label>
                </q-item-section>
                <q-item-section avatar>
                  <q-avatar color="warning" icon="lock" size="sm" text-color="white"/>
                </q-item-section>
              </q-item>
              <q-item v-if="retroBoard.locked" v-close-popup clickable @click="unlockTheBoard">
                <q-item-section>
                  <q-item-label>Unlock the board</q-item-label>
                </q-item-section>
                <q-item-section avatar>
                  <q-avatar color="positive" icon="lock_open" size="sm" text-color="white"/>
                </q-item-section>
              </q-item>
              <q-item v-close-popup clickable @click="exportTheBoard">
                <q-item-section>
                  <q-item-label>Export the board</q-item-label>
                </q-item-section>
                <q-item-section avatar>
                  <q-avatar color="primary" icon="download" size="sm" text-color="white"/>
                </q-item-section>
              </q-item>
              <q-item v-close-popup clickable @click="exit">
                <q-item-section>
                  <q-item-label>Exit</q-item-label>
                </q-item-section>
                <q-item-section avatar>
                  <q-avatar color="red-13" icon="logout" size="sm" text-color="white"/>
                </q-item-section>
              </q-item>
            </q-list>
          </q-btn-dropdown>
          <q-btn v-if="!userIsAuthor()" color="red-13" icon="logout" size="sm" @click="exit"/>
        </q-bar>
        <div class="col-xs-12 col-sm-6 col-md-3 col-lg-3 col-xl-3" style="padding: 30px">
          <div class="row">
            <div class="col-12">
              <q-input v-model="inputExpectColumn" bg-color="primary" bottom-slots color="white"
                       :label="retroBoardInput.firstColumnName" label-color="white"
                       :disable="getIsDisabled()" rounded standout type="text" @keydown.enter="sendExpectMessage">
              </q-input>
            </div>
          </div>
          <div class="row">
            <div class="col-12">
              <q-list>
                <template v-for="(card, index) in retroBoard.expectColumn" :key="index">
                  <div style="padding: 5px">
                    <q-card v-touch-hold="card.show" @mouseleave="card.show = false" @mouseover="card.show = true">
                      <q-card-section>
                        <div class="row q-pr-lg" v-bind:style="getBlur(card.username)">
                          {{ card.cardMessage }}
                        </div>
                        <q-badge class="row" color="transparent" floating>
                          <div class="row text-right">
                            <div class="col-12">
                              <q-badge color="orange">{{ card.username }}</q-badge>
                            </div>
                            <div class="col-12">
                              <q-badge color="secondary">{{ card.likes }}</q-badge>
                              <q-badge color="red-13">{{ card.dislikes }}</q-badge>
                            </div>
                          </div>
                        </q-badge>
                        <q-slide-transition>
                          <div v-show="card.show" class="column">
                            <div class="col self-end">
                              <q-btn v-if="!likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="red-13"
                                     icon="cancel" round size="sm"
                                     :disable="getIsDisabled()" @click="removeLike(card, index)"/>
                              <q-btn v-if="likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="secondary"
                                     icon="thumb_up" round size="sm"
                                     :disable="getIsDisabled()" @click="like(card, index)"/>
                              <q-btn v-if="likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="red-13"
                                     icon="thumb_down" round size="sm"
                                     :disable="getIsDisabled()" @click="dislike(card, index)"/>
                              <q-btn v-if="userIsAuthor()" :disable="getIsDisabled()"
                                     color="green-6" icon="add_circle" round
                                     size="sm" @click="enabledActionAlert(card, index)"/>
                              <q-btn v-if="userIsAuthorOrIsHisCard(card.username)" color="warning" icon="edit" round
                                     size="sm"
                                     :disable="getIsDisabled()" @click="enableAlert(card, index)"/>
                              <q-btn v-if="userIsAuthorOrIsHisCard(card.username)" color="negative" icon="delete" round
                                     size="sm"
                                     :disable="getIsDisabled()" @click="deleteCardExpectColumn(index)"/>
                            </div>
                          </div>
                        </q-slide-transition>
                      </q-card-section>
                      <q-card-section v-if="card.hasAction">
                        <div>Action:</div>
                        {{ card.actionMessage }}
                      </q-card-section>
                    </q-card>
                  </div>
                </template>
              </q-list>
            </div>
          </div>
        </div>
        <div class="col-xs-12 col-sm-6 col-md-3 col-lg-3 col-xl-3" style="padding: 30px">
          <div class="row">
            <div class="col-12">
              <q-input v-model="inputWentWellColumn" bg-color="secondary" bottom-slots color="white"
                       :label="retroBoardInput.secondColumnName"
                       label-color="white"
                       :disable="getIsDisabled()" rounded standout type="text" @keydown.enter="sendWellMessage">
              </q-input>
            </div>
          </div>
          <div class="row">
            <div class="col-12">
              <q-list>
                <template v-for="(card, index) in retroBoard.wentWellColumn" :key="index">
                  <div style="padding: 5px">
                    <q-card v-touch-hold="card.show" @mouseleave="card.show = false" @mouseover="card.show = true">
                      <q-card-section>
                        <div class="row q-pr-lg" v-bind:style="getBlur(card.username)">
                          {{ card.cardMessage }}
                        </div>
                        <q-badge class="row" color="transparent" floating>
                          <div class="row text-right">
                            <div class="col-12">
                              <q-badge color="orange">{{ card.username }}</q-badge>
                            </div>
                            <div class="col-12">
                              <q-badge color="secondary">{{ card.likes }}</q-badge>
                              <q-badge color="red-13">{{ card.dislikes }}</q-badge>
                            </div>
                          </div>
                        </q-badge>
                        <q-slide-transition>
                          <div v-show="card.show" class="column">
                            <div class="col self-end">
                              <q-btn v-if="!likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="red-13"
                                     icon="cancel" round size="sm"
                                     :disable="getIsDisabled()" @click="removeLike(card, index)"/>
                              <q-btn v-if="likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="secondary"
                                     icon="thumb_up" round size="sm"
                                     :disable="getIsDisabled()" @click="like(card, index)"/>
                              <q-btn v-if="likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="red-13"
                                     icon="thumb_down" round size="sm"
                                     :disable="getIsDisabled()" @click="dislike(card, index)"/>
                              <q-btn v-if="userIsAuthor()" :disable="getIsDisabled()"
                                     color="green-6" icon="add_circle" round
                                     size="sm" @click="enabledActionAlert(card, index)"/>
                              <q-btn v-if="userIsAuthorOrIsHisCard(card.username)" color="warning" icon="edit" round
                                     size="sm"
                                     :disable="getIsDisabled()" @click="enableAlert(card, index)"/>
                              <q-btn v-if="userIsAuthorOrIsHisCard(card.username)" color="negative" icon="delete" round
                                     size="sm"
                                     :disable="getIsDisabled()" @click="deleteCardWentWellColumn(index)"/>
                            </div>
                          </div>
                        </q-slide-transition>
                      </q-card-section>
                      <q-card-section v-if="card.hasAction">
                        <div>Action:</div>
                        {{ card.actionMessage }}
                      </q-card-section>
                    </q-card>
                  </div>
                </template>
              </q-list>
            </div>
          </div>
        </div>
        <div class="col-xs-12 col-sm-6 col-md-3 col-lg-3 col-xl-3" style="padding: 30px">
          <div class="row">
            <div class="col-12">
              <q-input v-model="inputDidNotGoWellColumn" bg-color="negative" bottom-slots color="white"
                       :label="retroBoardInput.thirdColumnName" label-color="white"
                       :disable="getIsDisabled()" rounded standout type="text" @keydown.enter="sendNotWellMessage">
              </q-input>
            </div>
          </div>
          <div class="row">
            <div class="col-12">
              <q-list>
                <template v-for="(card, index) in retroBoard.didNotGoWellColumn" :key="index">
                  <div style="padding: 5px">
                    <q-card v-touch-hold="card.show" @mouseleave="card.show = false" @mouseover="card.show = true">
                      <q-card-section>
                        <div class="row q-pr-lg" v-bind:style="getBlur(card.username)">
                          {{ card.cardMessage }}
                        </div>
                        <q-badge class="row" color="transparent" floating>
                          <div class="row text-right">
                            <div class="col-12">
                              <q-badge color="orange">{{ card.username }}</q-badge>
                            </div>
                            <div class="col-12">
                              <q-badge color="secondary">{{ card.likes }}</q-badge>
                              <q-badge color="red-13">{{ card.dislikes }}</q-badge>
                            </div>
                          </div>
                        </q-badge>
                        <q-slide-transition>
                          <div v-show="card.show" class="column">
                            <div class="col self-end">
                              <q-btn v-if="!likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="red-13"
                                     icon="cancel" round size="sm"
                                     :disable="getIsDisabled()" @click="removeLike(card, index)"/>
                              <q-btn v-if="likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="secondary"
                                     icon="thumb_up" round size="sm"
                                     :disable="getIsDisabled()" @click="like(card, index)"/>
                              <q-btn v-if="likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="red-13"
                                     icon="thumb_down" round size="sm"
                                     :disable="getIsDisabled()" @click="dislike(card, index)"/>
                              <q-btn v-if="userIsAuthor()" :disable="getIsDisabled()"
                                     color="green-6" icon="add_circle" round
                                     size="sm" @click="enabledActionAlert(card, index)"/>
                              <q-btn v-if="userIsAuthorOrIsHisCard(card.username)" color="warning" icon="edit" round
                                     size="sm"
                                     :disable="getIsDisabled()" @click="enableAlert(card, index)"/>
                              <q-btn v-if="userIsAuthorOrIsHisCard(card.username)" color="negative" icon="delete" round
                                     size="sm"
                                     :disable="getIsDisabled()" @click="deleteCardNotWellColumn(index)"/>
                            </div>
                          </div>
                        </q-slide-transition>
                      </q-card-section>
                      <q-card-section v-if="card.hasAction">
                        <div>Action:</div>
                        {{ card.actionMessage }}
                      </q-card-section>
                    </q-card>
                  </div>
                </template>
              </q-list>
            </div>
          </div>
        </div>
        <div class="col-xs-12 col-sm-6 col-md-3 col-lg-3 col-xl-3" style="padding: 30px">
          <div class="row">
            <div class="col-12">
              <q-input v-model="inputWantToTryColumn" bg-color="info" bottom-slots color="white"
                       :label="retroBoardInput.fourthColumnName" label-color="white"
                       :disable="getIsDisabled()" rounded standout type="text" @keydown.enter="sendTryMessage">
              </q-input>
            </div>
          </div>
          <div class="row">
            <div class="col-12">
              <q-list>
                <template v-for="(card, index) in retroBoard.wantToTryColumn" :key="index">
                  <div style="padding: 5px">
                    <q-card v-touch-hold="card.show" @mouseleave="card.show = false" @mouseover="card.show = true">
                      <q-card-section>
                        <div class="row q-pr-lg" v-bind:style="getBlur(card.username)">
                          {{ card.cardMessage }}
                        </div>
                        <q-badge class="row" color="transparent" floating>
                          <div class="row text-right">
                            <div class="col-12">
                              <q-badge color="orange">{{ card.username }}</q-badge>
                            </div>
                            <div class="col-12">
                              <q-badge color="secondary">{{ card.likes }}</q-badge>
                              <q-badge color="red-13">{{ card.dislikes }}</q-badge>
                            </div>
                          </div>
                        </q-badge>
                        <q-slide-transition>
                          <div v-show="card.show" class="column">
                            <div class="col self-end">
                              <q-btn v-if="!likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="red-13"
                                     icon="cancel" round size="sm"
                                     :disable="getIsDisabled()" @click="removeLike(card, index)"/>
                              <q-btn v-if="likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="secondary"
                                     icon="thumb_up" round size="sm"
                                     :disable="getIsDisabled()" @click="like(card, index)"/>
                              <q-btn v-if="likeButtonsVisible(card.uniqueId, card.likedOrDisliked)" color="red-13"
                                     icon="thumb_down" round size="sm"
                                     :disable="getIsDisabled()" @click="dislike(card, index)"/>
                              <q-btn v-if="userIsAuthor()" :disable="getIsDisabled()"
                                     color="green-6" icon="add_circle" round
                                     size="sm" @click="enabledActionAlert(card, index)"/>
                              <q-btn v-if="userIsAuthorOrIsHisCard(card.username)" color="warning" icon="edit"
                                     round
                                     size="sm"
                                     :disable="getIsDisabled()" @click="enableAlert(card, index)"/>
                              <q-btn v-if="userIsAuthorOrIsHisCard(card.username)" color="negative"
                                     icon="delete" round
                                     size="sm"
                                     :disable="getIsDisabled()" @click="deleteCardTryColumn(index)"/>
                            </div>
                          </div>
                        </q-slide-transition>
                      </q-card-section>
                      <q-card-section v-if="card.hasAction">
                        <div>Action:</div>
                        {{ card.actionMessage }}
                      </q-card-section>
                    </q-card>
                  </div>
                </template>
              </q-list>
            </div>
          </div>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
import {defineComponent, ref, watch} from 'vue'
import axios from 'axios'
import {stompClientStore} from 'stores/stomp'
import {storeToRefs} from "pinia";
import {copyToClipboard, Notify} from 'quasar'
import {jsPDF} from "jspdf";

const store = stompClientStore()
const {stompClientConnected} = storeToRefs(store)

export default defineComponent({
  name: 'BoardPage',
  data() {
    return {
      boardId: null,
      messageInput: null,
      messageInputVisible: false,
      joinCardVisible: false,
      createCardVisible: false,
      username: null,
      author: null,
      retroBoard: {
        id: null,
        author: null,
        firstColumnName: 'What I expected:',
        secondColumnName: 'Went well:',
        thirdColumnName: 'Went wrong:',
        fourthColumnName: 'What I want to try:',
        expectColumn: [],
        wentWellColumn: [],
        didNotGoWellColumn: [],
        wantToTryColumn: [],
        likedRecords: new Map(),
        users: [],
        locked: false,
        blurNumber: 0,
        blurActive: false
      },
      inputWentWellColumn: null,
      inputExpectColumn: null,
      inputDidNotGoWellColumn: null,
      inputWantToTryColumn: null,
      createUsernameValid: false,
      joinUsernameValid: false,
      boardIdValid: false,
      retroBoardMessage: {
        uniqueId: null,
        username: null,
        cardMessage: null,
        columnType: null,
        index: null,
        show: false,
        likes: 0,
        dislikes: 0,
        likedOrDisliked: false,
        actionMessage: null,
        hasAction: false
      },
      boardIdErrorMessage: 'Board ID required!',
      usernameErrorMessage: 'Username required!',
      ratingModel: ref(3),
      menu: false,
      noWebsocketConnectionVisible: false,
      spinnerVisible: false,
      alert: false,
      alertMessage: null,
      deleteAlert: false,
      deleteAlertIndex: null,
      deleteAlertMessage: null,
      actionAlert: false,
      actionAlertIndex: null,
      actionAlertMessage: null,
      subscriptions: [],
      numberOfActiveRetroBoards: 0,
      retroBoardInput: {
        id: null,
        author: null,
        firstColumnName: 'What I expected:',
        secondColumnName: 'Went well:',
        thirdColumnName: 'Went wrong:',
        fourthColumnName: 'What I want to try:'
      },
      createBoardAlert: false
    }
  },
  created() {
    this.spinnerVisible = true
    setTimeout(this.reload, 2000)
    watch(stompClientConnected, () => {
      if (!stompClientConnected) {
        this.exit()
      }
    })
    window.addEventListener("beforeunload", this.exit)
  },
  beforeUnmount() {
    store.getStompClient.send("/app/board/" + this.boardId + "/" + this.username + "/user.remove", {});
  },
  unmounted() {
    this.subscriptions.forEach(function (subscription) {
      subscription.unsubscribe()
    })
  },
  methods: {
    reload() {
      this.getNumberOfActiveRetroBoards()
      if (stompClientStore().getStompClientStatus) {
        if (stompClientStore().getRetroBoardId) {
          this.boardId = stompClientStore().getRetroBoardId
          this.username = stompClientStore().getUsername
          this.author = stompClientStore().getAuthor
          axios.get(`https://www.retrospecto.cloud/board/` + this.boardId + '/' + this.username)
            .then(response => {
              if (response.status === 200) {
                this.spinnerVisible = false
                this.retroBoard = response.data
                this.retroBoard.likedRecords = new Map(Object.entries(this.retroBoard.likedRecords))
                this.messageInputVisible = true
                this.joinCardVisible = false
                this.createCardVisible = false
                this.subscribe()
              }
            }).catch(error => {
            if (error.response.status === 302) {
              this.spinnerVisible = false
              this.retroBoard = error.response.data
              this.retroBoard.likedRecords = new Map(Object.entries(this.retroBoard.likedRecords))
              this.messageInputVisible = true
              this.joinCardVisible = false
              this.createCardVisible = false
              this.subscribe()
            } else {
              this.spinnerVisible = false
              this.noWebsocketConnectionVisible = true
              this.joinCardVisible = false
              this.createCardVisible = false
              this.messageInputVisible = false
              console.log(error)
            }
          })
        } else {
          if (this.$route.params.boardId != null) {
            this.boardId = this.$route.params.boardId
            this.createCardVisible = false
          } else {
            this.createCardVisible = true
          }
          this.spinnerVisible = false
          this.joinCardVisible = true
          this.messageInputVisible = false
        }
      } else {
        this.spinnerVisible = false
        this.noWebsocketConnectionVisible = true
        this.joinCardVisible = false
        this.createCardVisible = false
        this.messageInputVisible = false
      }
      this.username = localStorage.getItem('board-username')
      this.author = localStorage.getItem('board-author')
    },
    deleteCardExpectColumn(key) {
      this.deleteAlertMessage = this.retroBoard.expectColumn[key]
      this.deleteAlertIndex = key
      this.deleteAlert = true
    },
    deleteCardWentWellColumn(key) {
      this.deleteAlertMessage = this.retroBoard.wentWellColumn[key]
      this.deleteAlertIndex = key
      this.deleteAlert = true
    },
    deleteCardNotWellColumn(key) {
      this.deleteAlertMessage = this.retroBoard.didNotGoWellColumn[key]
      this.deleteAlertIndex = key
      this.deleteAlert = true
    },
    deleteCardTryColumn(key) {
      this.deleteAlertMessage = this.retroBoard.wantToTryColumn[key]
      this.deleteAlertIndex = key
      this.deleteAlert = true
    },
    deleteCard(retroBoardMessage, key) {
      retroBoardMessage.index = key
      store.getStompClient.send("/app/board/" + this.boardId + "/card.delete", {}, JSON.stringify(retroBoardMessage));
    },
    sendExpectMessage() {
      this.retroBoardMessage = {
        username: this.username,
        cardMessage: this.inputExpectColumn,
        columnType: 'EXPECT'
      }
      if (this.inputExpectColumn != null) {
        this.sendCardMessage()
        this.inputExpectColumn = null
      }
    },
    sendWellMessage() {
      this.retroBoardMessage = {
        username: this.username,
        cardMessage: this.inputWentWellColumn,
        columnType: 'WELL'
      }
      if (this.inputWentWellColumn != null) {
        this.sendCardMessage()
        this.inputWentWellColumn = null
      }
    },
    sendNotWellMessage() {
      this.retroBoardMessage = {
        username: this.username,
        cardMessage: this.inputDidNotGoWellColumn,
        columnType: 'NOT_WELL'
      }
      if (this.inputDidNotGoWellColumn != null) {
        this.sendCardMessage()
        this.inputDidNotGoWellColumn = null
      }
    },
    sendTryMessage() {
      this.retroBoardMessage = {
        username: this.username,
        cardMessage: this.inputWantToTryColumn,
        columnType: 'TRY'
      }
      if (this.inputWantToTryColumn != null) {
        this.sendCardMessage()
        this.inputWantToTryColumn = null
      }
    },
    sendCardMessage() {
      store.getStompClient.send("/app/board/" + this.boardId + "/card.add", {}, JSON.stringify(this.retroBoardMessage));
    },
    sendEditMessage() {
      let updatedMessage = this.alertMessage
      store.getStompClient.send("/app/board/" + this.boardId + "/card.edit", {}, JSON.stringify(updatedMessage));
    },
    like(message, index) {
      message.index = index
      message.likedOrDisliked = true
      store.getStompClient.send("/app/board/" + this.boardId + "/" + this.username + "/card.like", {}, JSON.stringify(message));
    },
    dislike(message, index) {
      message.index = index
      message.likedOrDisliked = true
      store.getStompClient.send("/app/board/" + this.boardId + "/" + this.username + "/card.dislike", {}, JSON.stringify(message));
    },
    removeLike(message, index) {
      message.index = index
      message.likedOrDisliked = false
      store.getStompClient.send("/app/board/" + this.boardId + "/" + this.username + "/card.removeLike", {}, JSON.stringify(message));
    },
    enabledActionAlert(message, index) {
      this.actionAlertMessage = message
      this.actionAlertIndex = index
      this.actionAlert = true
    },
    addAction(message, index) {
      message.index = index
      message.hasAction = true
      message.actionMessage = this.actionAlertMessage.actionMessage
      store.getStompClient.send("/app/board/" + this.boardId + "/action.add", {}, JSON.stringify(message));
    },
    likeButtonsVisible(uniqueId, likedOrDisliked) {
      try {
        const map = this.retroBoard.likedRecords
        if (map.has(this.username)) {
          if (map.get(this.username).includes(uniqueId)) {
            return false
          } else {
            return !likedOrDisliked
          }
        }
      } catch (error) {
        return !likedOrDisliked
      }
    },
    onAddMessageReceived(payload) {
      let retroBoardMessage = JSON.parse(payload.body);
      if (retroBoardMessage.columnType === 'EXPECT') {
        this.retroBoard.expectColumn.push(retroBoardMessage)
      }
      if (retroBoardMessage.columnType === 'WELL') {
        this.retroBoard.wentWellColumn.push(retroBoardMessage)
      }
      if (retroBoardMessage.columnType === 'NOT_WELL') {
        this.retroBoard.didNotGoWellColumn.push(retroBoardMessage)
      }
      if (retroBoardMessage.columnType === 'TRY') {
        this.retroBoard.wantToTryColumn.push(retroBoardMessage)
      }
    },
    onDeleteMessageReceived(payload) {
      let retroBoardMessage = JSON.parse(payload.body);
      if (retroBoardMessage.columnType === 'EXPECT') {
        this.retroBoard.expectColumn.splice(retroBoardMessage.index, 1)
      }
      if (retroBoardMessage.columnType === 'WELL') {
        this.retroBoard.wentWellColumn.splice(retroBoardMessage.index, 1)
      }
      if (retroBoardMessage.columnType === 'NOT_WELL') {
        this.retroBoard.didNotGoWellColumn.splice(retroBoardMessage.index, 1)
      }
      if (retroBoardMessage.columnType === 'TRY') {
        this.retroBoard.wantToTryColumn.splice(retroBoardMessage.index, 1)
      }
    },
    onEditMessageReceived(payload) {
      let retroBoardMessage = JSON.parse(payload.body);
      if (retroBoardMessage.columnType === 'EXPECT') {
        this.retroBoard.expectColumn[retroBoardMessage.index].cardMessage = retroBoardMessage.cardMessage
      }
      if (retroBoardMessage.columnType === 'WELL') {
        this.retroBoard.wentWellColumn[retroBoardMessage.index].cardMessage = retroBoardMessage.cardMessage
      }
      if (retroBoardMessage.columnType === 'NOT_WELL') {
        this.retroBoard.didNotGoWellColumn[retroBoardMessage.index].cardMessage = retroBoardMessage.cardMessage
      }
      if (retroBoardMessage.columnType === 'TRY') {
        this.retroBoard.wantToTryColumn[retroBoardMessage.index].cardMessage = retroBoardMessage.cardMessage
      }
    },
    onActionMessageReceived(payload) {
      let retroBoardMessage = JSON.parse(payload.body);
      if (retroBoardMessage.columnType === 'EXPECT') {
        this.retroBoard.expectColumn[retroBoardMessage.index].actionMessage = retroBoardMessage.actionMessage
        this.retroBoard.expectColumn[retroBoardMessage.index].hasAction = true
      }
      if (retroBoardMessage.columnType === 'WELL') {
        this.retroBoard.wentWellColumn[retroBoardMessage.index].actionMessage = retroBoardMessage.actionMessage
        this.retroBoard.wentWellColumn[retroBoardMessage.index].hasAction = true
      }
      if (retroBoardMessage.columnType === 'NOT_WELL') {
        this.retroBoard.didNotGoWellColumn[retroBoardMessage.index].actionMessage = retroBoardMessage.actionMessage
        this.retroBoard.didNotGoWellColumn[retroBoardMessage.index].hasAction = true
      }
      if (retroBoardMessage.columnType === 'TRY') {
        this.retroBoard.wantToTryColumn[retroBoardMessage.index].actionMessage = retroBoardMessage.actionMessage
        this.retroBoard.wantToTryColumn[retroBoardMessage.index].hasAction = true
      }
    },
    onLikeMessageReceived(payload) {
      let retroBoardMessage = JSON.parse(payload.body);
      if (retroBoardMessage.columnType === 'EXPECT') {
        this.retroBoard.expectColumn[retroBoardMessage.index].likes = retroBoardMessage.likes
        this.retroBoard.expectColumn[retroBoardMessage.index].dislikes = retroBoardMessage.dislikes
      }
      if (retroBoardMessage.columnType === 'WELL') {
        this.retroBoard.wentWellColumn[retroBoardMessage.index].likes = retroBoardMessage.likes
        this.retroBoard.wentWellColumn[retroBoardMessage.index].dislikes = retroBoardMessage.dislikes
      }
      if (retroBoardMessage.columnType === 'NOT_WELL') {
        this.retroBoard.didNotGoWellColumn[retroBoardMessage.index].likes = retroBoardMessage.likes
        this.retroBoard.didNotGoWellColumn[retroBoardMessage.index].dislikes = retroBoardMessage.dislikes
      }
      if (retroBoardMessage.columnType === 'TRY') {
        this.retroBoard.wantToTryColumn[retroBoardMessage.index].likes = retroBoardMessage.likes
        this.retroBoard.wantToTryColumn[retroBoardMessage.index].dislikes = retroBoardMessage.dislikes
      }
    },
    onReorderMessageReceived(payload) {
      this.retroBoard = JSON.parse(payload.body)
      this.retroBoard.likedRecords = new Map(Object.entries(this.retroBoard.likedRecords))
    },
    onUserMessageReceived(payload) {
      this.retroBoard.users = JSON.parse(payload.body)
    },
    enableAlert(message, index) {
      this.alert = true
      this.alertMessage = message
      this.alertMessage.index = index
    },
    orderByLikes() {
      store.getStompClient.send("/app/board/" + this.boardId + "/order.like", {}, JSON.stringify(null));
    },
    orderByDislikes() {
      store.getStompClient.send("/app/board/" + this.boardId + "/order.dislike", {}, JSON.stringify(null));
    },
    subscribe() {
      localStorage.setItem('board-username', this.username)
      this.subscriptions.push(store.getStompClient.subscribe('/topic/board/' + this.boardId + '/add', this.onAddMessageReceived))
      this.subscriptions.push(store.getStompClient.subscribe('/topic/board/' + this.boardId + '/delete', this.onDeleteMessageReceived))
      this.subscriptions.push(store.getStompClient.subscribe('/topic/board/' + this.boardId + '/edit', this.onEditMessageReceived))
      this.subscriptions.push(store.getStompClient.subscribe('/topic/board/' + this.boardId + '/like', this.onLikeMessageReceived))
      this.subscriptions.push(store.getStompClient.subscribe('/topic/board/' + this.boardId + '/reorder', this.onReorderMessageReceived))
      this.subscriptions.push(store.getStompClient.subscribe('/topic/board/' + this.boardId + '/user', this.onUserMessageReceived))
      this.subscriptions.push(store.getStompClient.subscribe('/topic/board/' + this.boardId + '/locking', this.onLockMessageReceived))
      this.subscriptions.push(store.getStompClient.subscribe('/topic/board/' + this.boardId + '/blur', this.onBlurMessageReceived))
      this.subscriptions.push(store.getStompClient.subscribe('/topic/board/' + this.boardId + '/action', this.onActionMessageReceived))
      store.getStompClient.send("/app/board/" + this.boardId + "/" + this.username + "/user.add", {});
      stompClientStore().setUsernameAuthorBoardId(this.username, this.retroBoard.author, this.boardId)
    },
    exit() {
      this.getNumberOfActiveRetroBoards()
      this.subscriptions.forEach(function (subscription) {
        subscription.unsubscribe()
      })
      store.getStompClient.send("/app/board/" + this.boardId + "/" + this.username + "/user.remove", {});
      stompClientStore().setUsernameAuthorBoardId(localStorage.getItem('board-username'), localStorage.getItem('board-author'), null)
      this.messageInputVisible = false
      this.joinCardVisible = true
      this.createCardVisible = true
      this.username = localStorage.getItem('board-username')
      this.boardId = null
      this.retroBoard = null
      this.author = localStorage.getItem('board-author')
    },
    getFirstLetter(username) {
      return Array.from(username)[0];
    },
    userIsAuthor() {
      return this.username === this.retroBoard.author
    },
    userIsAuthorOrIsHisCard(cardUsername) {
      if (this.retroBoard.author === this.username) {
        return true
      } else if (this.username === cardUsername) {
        return true
      } else {
        return false
      }
    },
    lockTheBoard() {
      store.getStompClient.send("/app/board/" + this.boardId + "/" + this.username + "/lock", {}, JSON.stringify(null));
    },
    unlockTheBoard() {
      store.getStompClient.send("/app/board/" + this.boardId + "/" + this.username + "/unlock", {}, JSON.stringify(null));
    },
    exportTheBoard() {
      const width = document.getElementById('boardPage').getBoundingClientRect().width
      const height = document.getElementById('boardPage').getBoundingClientRect().height
      const doc = new jsPDF('l', 'px', [height / 2.22, width / 2.22]);

      doc.html(document.getElementById('boardPage'), {
        html2canvas: {
          scale: 0.45,
          backgroundColor: 'GhostWhite',
          width: width,
          height: height,
          windowWidth: width,
          windowHeight: height
        },
        callback: function (doc) {
          doc.save('retroboard.pdf');
        }
      });
    },
    onLockMessageReceived(payload) {
      const response = JSON.parse(payload.body)
      if (response.statusCodeValue === 200) {
        this.retroBoard = response.body
      } else {
        console.log('Error with locking the board:')
        console.log(response)
      }
    },
    onBlurMessageReceived(payload) {
      const response = JSON.parse(payload.body)
      if (response.statusCodeValue === 200) {
        this.retroBoard = response.body
      } else {
        console.log('Error with bluring the board:')
        console.log(response)
      }
    },
    getIsDisabled() {
      if (this.userIsAuthor()) {
        return false
      }
      return this.retroBoard.locked
    },
    getNumberOfActiveRetroBoards() {
      axios.get(`https://www.retrospecto.cloud/board/number`)
        .then(response => {
          this.numberOfActiveRetroBoards = response.data
        })
    },
    shareTheBoard() {
      copyToClipboard('https://www.retrospecto.cloud/#/board/' + this.boardId).then(() => {
        // success!
        Notify.create({
          message: 'URL copied to clipboard!',
          color: 'blue',
          position: "center"
        })
      })
        .catch(() => {
          // fail
          Notify.create({
            message: 'URL copy failed!',
            color: 'red',
            position: "center"
          })
        })
    },
    blurCardText() {
      store.getStompClient.send("/app/board/" + this.boardId + "/" + this.username + "/blur", {}, JSON.stringify(null));
    },
    unblurCardText() {
      store.getStompClient.send("/app/board/" + this.boardId + "/" + this.username + "/unblur", {}, JSON.stringify(null));
    },
    getBlur(username) {
      if (this.username === username) {
        return '-webkit-filter: blur(0px)'
      } else {
        return '-webkit-filter: blur(' + this.retroBoard.blurNumber + 'px)'
      }
    },
    joinBoard() {
      if (!this.boardId && !this.username) {
        this.joinUsernameValid = true
        this.boardIdValid = true
        this.boardIdErrorMessage = "Board ID required!"
      }
      if (!this.username && this.boardId) {
        this.joinUsernameValid = true
        this.boardIdValid = false
      }
      if (this.username && !this.boardId) {
        this.boardIdValid = true
        this.boardIdErrorMessage = "Board ID required!"
        this.joinUsernameValid = false
      }
      if (this.boardId && this.username) {
        this.joinUsernameValid = false
        this.boardIdValid = false
        axios.get(`https://www.retrospecto.cloud/board/` + this.boardId + '/' + this.username)
          .then(response => {
            if (response.status === 200) {
              this.retroBoard = response.data
              this.subscribe()
              this.messageInputVisible = true
              this.joinCardVisible = false
              this.createCardVisible = false
            }
          })
          .catch(error => {
            if (error.response.status === 302) {
              this.joinUsernameValid = true
              this.usernameErrorMessage = 'Username is already taken.'
              this.username = null
            } else {
              this.boardIdValid = true
              this.boardIdErrorMessage = 'Board ID does not exist!'
              this.boardId = null
              console.log(error)
            }
          })
      }
    },
    onSubmit() {
        this.retroBoardInput.author = this.author
        axios.post(`https://www.retrospecto.cloud/board/create/`, this.retroBoardInput)
          .then(response => {
            // JSON responses are automatically parsed.
            if (response.data != null) {
              this.createBoardAlert = false
              this.retroBoard = response.data
              this.boardId = response.data.id
              this.createUsernameValid = false
              this.username = this.author
              this.messageInputVisible = true
              this.joinCardVisible = false
              this.createCardVisible = false
              localStorage.setItem('board-author', this.author)
              this.subscribe()
            }
          })
    },
    createBoard() {
      if (!this.author) {
        this.createUsernameValid = true
      } else {
        this.createBoardAlert = true
      }
    }
  }
})
</script>

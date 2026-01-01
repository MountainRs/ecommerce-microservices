<template>
  <div class="max-w-2xl mx-auto space-y-6">
    <h1 class="text-3xl font-bold">个人资料</h1>

    <div v-if="user" class="card space-y-6">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <label class="form-label">用户名</label>
          <input
            v-model="form.username"
            type="text"
            disabled
            class="form-input bg-gray-100"
          />
        </div>

        <div>
          <label class="form-label">邮箱</label>
          <input
            v-model="form.email"
            type="email"
            disabled
            class="form-input bg-gray-100"
          />
        </div>

        <div>
          <label class="form-label">真实姓名</label>
          <input
            v-model="form.realName"
            type="text"
            class="form-input"
            placeholder="请输入真实姓名"
          />
        </div>

        <div>
          <label class="form-label">手机号</label>
          <input
            v-model="form.phone"
            type="tel"
            class="form-input"
            placeholder="请输入手机号"
          />
        </div>
      </div>

      <div v-if="error" class="rounded-md bg-red-50 p-4">
        <p class="text-sm font-medium text-red-800">{{ error }}</p>
      </div>

      <div v-if="success" class="rounded-md bg-green-50 p-4">
        <p class="text-sm font-medium text-green-800">{{ success }}</p>
      </div>

      <div class="flex gap-4">
        <button
          @click="handleUpdate"
          :disabled="loading"
          class="btn-primary disabled:opacity-50"
        >
          {{ loading ? '保存中...' : '保存修改' }}
        </button>
        <button @click="handleCancel" class="btn-secondary">
          取消
        </button>
      </div>
    </div>

    <div v-else class="text-center py-12">
      <p class="text-gray-600">加载中...</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const user = ref<any>(null)
const form = ref({
  username: '',
  email: '',
  realName: '',
  phone: '',
})

const loading = ref(false)
const error = ref('')
const success = ref('')

const loadUserInfo = () => {
  if (userStore.user) {
    user.value = userStore.user
    form.value = {
      username: userStore.user.username,
      email: userStore.user.email,
      realName: userStore.user.realName || '',
      phone: userStore.user.phone || '',
    }
  }
}

const handleUpdate = async () => {
  if (!user.value) return

  loading.value = true
  error.value = ''
  success.value = ''

  try {
    await userStore.updateUserInfo({
      realName: form.value.realName,
      phone: form.value.phone,
    })
    success.value = '个人信息更新成功'
    loadUserInfo()
  } catch (err) {
    error.value = '更新失败，请稍后重试'
    console.error(err)
  } finally {
    loading.value = false
  }
}

const handleCancel = () => {
  loadUserInfo()
  error.value = ''
  success.value = ''
}

onMounted(() => {
  loadUserInfo()
})
</script>

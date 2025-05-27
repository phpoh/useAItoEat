# ---------- 构建阶段 ----------
FROM node:18-alpine as build-stage

WORKDIR /app

# 复制 package.json 和 package-lock.json（或者 yarn.lock）
COPY package*.json ./

# 安装依赖
RUN npm install --legacy-peer-deps

# 复制所有项目文件（包含你的 vite.config.js）
COPY . .

# 执行打包，生成 dist 文件夹
RUN npm run build

# ---------- 生产运行阶段 ----------
FROM nginx:stable-alpine as production-stage

# 删除默认 nginx 静态资源（可选）
RUN rm -rf /usr/share/nginx/html/*

# 复制构建产物到 nginx 默认目录
COPY --from=build-stage /app/dist /usr/share/nginx/html

# 暴露容器 80 端口
EXPOSE 80

# 启动 nginx 服务器
CMD ["nginx", "-g", "daemon off;"]

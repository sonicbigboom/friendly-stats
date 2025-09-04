# Build Stage
FROM node:23-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
ARG REACT_APP_FRIENDLY_STATS_API_HOST
ENV REACT_APP_FRIENDLY_STATS_API_HOST $REACT_APP_FRIENDLY_STATS_API_HOST
ARG REACT_APP_FRIENDLY_STATS_WEB_HOST
ENV REACT_APP_FRIENDLY_STATS_WEB_HOST $REACT_APP_FRIENDLY_STATS_WEB_HOST
ARG REACT_APP_FRIENDLY_STATS_GOOGLE_CLIENT_ID
ENV REACT_APP_FRIENDLY_STATS_GOOGLE_CLIENT_ID $REACT_APP_FRIENDLY_STATS_GOOGLE_CLIENT_ID
COPY . .
RUN npm run build

# Production Stage
FROM nginx:stable-alpine AS production
COPY --from=build /app/build /usr/share/nginx/html
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d
CMD ["nginx", "-g", "daemon off;"]
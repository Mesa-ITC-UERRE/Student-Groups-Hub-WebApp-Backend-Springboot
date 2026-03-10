-- V1__initial_schema.sql
-- Initial schema for Student Groups Platform (generated from Hibernate DDL output)
-- Updated to include 'admin' in role_assignments.permission_role

-- =========================
-- TABLES
-- =========================

create table users (
  is_platform_admin boolean not null,
  created_at timestamptz not null,
  updated_at timestamptz not null,
  id uuid not null,
  entra_oid varchar(200) not null unique,
  email varchar(320) not null unique,
  avatar_url varchar(255),
  display_name varchar(255),
  primary key (id)
);

create table groups (
  is_active boolean not null,
  created_at timestamptz not null,
  updated_at timestamptz not null,
  id uuid not null,
  description text,
  logo_url varchar(255),
  name varchar(255) not null,
  slug varchar(255) not null unique,
  primary key (id)
);

create table group_terms (
  end_date date not null,
  start_date date not null,
  created_at timestamptz not null,
  updated_at timestamptz not null,
  group_id uuid not null,
  id uuid not null,
  label varchar(255) not null,
  status varchar(255) not null check (status in ('draft','active','archived')),
  primary key (id),
  constraint uq_group_terms_group_label unique (group_id, label)
);

create table posts (
  is_pinned boolean not null,
  created_at timestamptz not null,
  pinned_at timestamptz,
  updated_at timestamptz not null,
  author_user_id uuid not null,
  group_id uuid not null,
  id uuid not null,
  term_id uuid,
  body text,
  title varchar(255) not null,
  type varchar(255) not null check (type in ('activity','event','announcement','general')),
  visibility varchar(255) not null check (visibility in ('public_','members_only')),
  primary key (id)
);

create table comments (
  created_at timestamptz not null,
  updated_at timestamptz not null,
  author_user_id uuid not null,
  id uuid not null,
  post_id uuid not null,
  body text not null,
  primary key (id)
);

create table events (
  created_at timestamptz not null,
  end_at timestamptz not null,
  start_at timestamptz not null,
  updated_at timestamptz not null,
  created_by_user_id uuid not null,
  group_id uuid not null,
  id uuid not null,
  term_id uuid,
  description text,
  location varchar(255),
  status varchar(255) not null check (status in ('draft','published','cancelled')),
  timezone varchar(255) not null,
  title varchar(255) not null,
  visibility varchar(255) not null check (visibility in ('public_','members_only')),
  primary key (id)
);

create table event_rsvps (
  created_at timestamptz not null,
  updated_at timestamptz not null,
  event_id uuid not null,
  id uuid not null,
  user_id uuid not null,
  status varchar(255) not null check (status in ('going','interested','not_going')),
  primary key (id),
  constraint uq_event_rsvps_event_user unique (event_id, user_id)
);

create table group_memberships (
  created_at timestamptz not null,
  joined_at timestamptz,
  left_at timestamptz,
  requested_at timestamptz not null,
  reviewed_at timestamptz,
  updated_at timestamptz not null,
  group_id uuid not null,
  id uuid not null,
  reviewed_by_user_id uuid,
  user_id uuid not null,
  decision_notes text,
  status varchar(255) not null check (status in ('pending','approved','rejected','left','removed','banned')),
  primary key (id),
  constraint uq_group_memberships_group_user unique (group_id, user_id)
);

create table group_registration_requests (
  created_at timestamptz not null,
  reviewed_at timestamptz,
  updated_at timestamptz not null,
  id uuid not null,
  requested_by_user_id uuid not null,
  reviewed_by_user_id uuid,
  contact_email varchar(255) not null,
  decision_notes text,
  proposed_description text,
  proposed_group_name varchar(255) not null,
  status varchar(255) not null check (status in ('pending','approved','rejected')),
  primary key (id)
);

create table notifications (
  is_read boolean not null,
  created_at timestamptz not null,
  updated_at timestamptz not null,
  id uuid not null,
  user_id uuid not null,
  body text,
  href varchar(255),
  kind varchar(255) not null,
  title varchar(255) not null,
  primary key (id)
);

create table post_media (
  sort_order integer not null,
  created_at timestamptz not null,
  updated_at timestamptz not null,
  id uuid not null,
  post_id uuid not null,
  media_type varchar(255) not null,
  url varchar(255) not null,
  primary key (id)
);

create table reports (
  created_at timestamptz not null,
  reviewed_at timestamptz,
  updated_at timestamptz not null,
  id uuid not null,
  reported_by_user_id uuid not null,
  reviewed_by_user_id uuid,
  target_id uuid not null,
  details text,
  reason varchar(255) not null,
  status varchar(255) not null check (status in ('open','reviewed','dismissed','action_taken')),
  target_type varchar(255) not null check (target_type in ('post','event','group','user')),
  primary key (id)
);

create table role_assignments (
  end_date date,
  start_date date,
  created_at timestamptz not null,
  updated_at timestamptz not null,
  created_by_user_id uuid,
  group_id uuid not null,
  id uuid not null,
  term_id uuid,
  user_id uuid not null,
  display_role varchar(255),
  permission_role varchar(255) not null check (permission_role in ('president','vice','secretary','treasurer','admin','member','staff')),
  primary key (id)
);

-- =========================
-- INDEXES
-- =========================

create index ix_comments_post_created on comments (post_id, created_at);
create index ix_comments_author_created on comments (author_user_id, created_at);

create index ix_event_rsvps_user_status on event_rsvps (user_id, status);

create index ix_events_start_at on events (start_at);
create index ix_events_group_start_at on events (group_id, start_at);
create index ix_events_term_start_at on events (term_id, start_at);

create index ix_group_memberships_group_status on group_memberships (group_id, status);
create index ix_group_memberships_user_status on group_memberships (user_id, status);

create index ix_group_reg_requests_status_created on group_registration_requests (status, created_at);
create index ix_group_reg_requests_requested_by on group_registration_requests (requested_by_user_id, created_at);

create index ix_notifications_user_read_created on notifications (user_id, is_read, created_at);

create index ix_post_media_post_sort on post_media (post_id, sort_order);

create index ix_posts_group_created on posts (group_id, created_at);
create index ix_posts_group_pinned on posts (group_id, is_pinned);
create index ix_posts_term_created on posts (term_id, created_at);

create index ix_reports_target on reports (target_type, target_id);
create index ix_reports_status_created on reports (status, created_at);

create index ix_role_assignments_group_term_role on role_assignments (group_id, term_id, permission_role);
create index ix_role_assignments_group_user on role_assignments (group_id, user_id);

-- =========================
-- FOREIGN KEYS
-- =========================

alter table comments
  add constraint fk_comments_author_user
  foreign key (author_user_id) references users (id);

alter table comments
  add constraint fk_comments_post
  foreign key (post_id) references posts (id);

alter table posts
  add constraint fk_posts_author_user
  foreign key (author_user_id) references users (id);

alter table posts
  add constraint fk_posts_group
  foreign key (group_id) references groups (id);

alter table posts
  add constraint fk_posts_term
  foreign key (term_id) references group_terms (id);

alter table events
  add constraint fk_events_created_by_user
  foreign key (created_by_user_id) references users (id);

alter table events
  add constraint fk_events_group
  foreign key (group_id) references groups (id);

alter table events
  add constraint fk_events_term
  foreign key (term_id) references group_terms (id);

alter table event_rsvps
  add constraint fk_event_rsvps_event
  foreign key (event_id) references events (id);

alter table event_rsvps
  add constraint fk_event_rsvps_user
  foreign key (user_id) references users (id);

alter table group_memberships
  add constraint fk_group_memberships_group
  foreign key (group_id) references groups (id);

alter table group_memberships
  add constraint fk_group_memberships_reviewed_by_user
  foreign key (reviewed_by_user_id) references users (id);

alter table group_memberships
  add constraint fk_group_memberships_user
  foreign key (user_id) references users (id);

alter table group_registration_requests
  add constraint fk_group_reg_requests_requested_by_user
  foreign key (requested_by_user_id) references users (id);

alter table group_registration_requests
  add constraint fk_group_reg_requests_reviewed_by_user
  foreign key (reviewed_by_user_id) references users (id);

alter table group_terms
  add constraint fk_group_terms_group
  foreign key (group_id) references groups (id);

alter table notifications
  add constraint fk_notifications_user
  foreign key (user_id) references users (id);

alter table post_media
  add constraint fk_post_media_post
  foreign key (post_id) references posts (id);

alter table reports
  add constraint fk_reports_reported_by_user
  foreign key (reported_by_user_id) references users (id);

alter table reports
  add constraint fk_reports_reviewed_by_user
  foreign key (reviewed_by_user_id) references users (id);

alter table role_assignments
  add constraint fk_role_assignments_created_by_user
  foreign key (created_by_user_id) references users (id);

alter table role_assignments
  add constraint fk_role_assignments_group
  foreign key (group_id) references groups (id);

alter table role_assignments
  add constraint fk_role_assignments_term
  foreign key (term_id) references group_terms (id);

alter table role_assignments
  add constraint fk_role_assignments_user
  foreign key (user_id) references users (id);
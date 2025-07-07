# Orange School API Server - Domain Modules Overview

## Project Architecture

The Orange School API Server is a Spring Boot application designed for managing educational activities, schedules, and social interactions between parents and children. The application follows a domain-driven design with clear module separation.

## Core Domain Modules

### 1. **CommonMember Module**
**Primary Responsibility**: User management and authentication for both parents and children

**Entities**:
- `CommonMember` - Central user entity supporting both parent and child accounts
  - Relationships: Connected to almost all other modules (schedules, challenges, stories, etc.)
  - Key fields: memberType (PARENT/CHILD), email, nickName, parentId, referralCode

**Services**:
- `CommonMemberService` - User registration, authentication, profile management
- `CommonMemberServiceV2` - Enhanced version with additional features
- `TownFriendService` - Regional community features

**Controller Endpoints**:
- `/api/common/join` - User registration
- `/api/common/check/email` - Email validation
- `/api/common/check/nickname` - Nickname validation
- `/api/common/check/phoneNumber` - SMS verification
- `/api/user/member/*` - Profile management
- `/api/user/townFriend/*` - Regional friend features

**Key Features**:
- Dual account types (Parent/Child)
- Referral system with codes
- Regional tagging for community features
- SMS verification
- Social login support

### 2. **Academy Module**
**Primary Responsibility**: Educational institution management

**Entities**:
- `Academy` - Represents educational institutions
  - Fields: academyName, address, isAdmin

**Relationships**:
- Connected to CommonMember through `MemberAcademy` (many-to-many)
- Referenced in Schedule for academy-related schedules

**Controller Endpoints**:
- `/api/user/academy` - User academy registration
- `/api/admin/academy` - Admin academy management
- `/api/user/academies` - List academies

### 3. **Schedule Module**
**Primary Responsibility**: Comprehensive schedule and calendar management

**Entities**:
- `Schedule` - Main scheduling entity
  - Supports various types: ACADEMY, SCHOOL, CLASS, VEHICLE, PAY, etc.
  - Features: Recurring schedules, payment tracking, alarms
- `DayMessage` - Daily messages/notes
- `TodoList` - Task management
- `SleepInfo` - Sleep tracking for children

**Related Entities**:
- `Calendar` - Individual calendar entries generated from schedules
- `ScheduleAlarm` - Alarm configurations

**Controller Endpoints**:
- `/api/user/schedule` - Schedule CRUD operations
- `/api/user/payment` - Payment schedule management
- `/api/user/timeTable/*` - Timetable features
- `/api/user/sleep/*` - Sleep tracking

**Key Features**:
- Recurring schedule support
- Payment tracking and reminders
- Multiple alarm types for parents and children
- Integration with academy schedules

### 4. **Challenge Module**
**Primary Responsibility**: Gamification through challenges and rewards

**Entities**:
- `Challenge` - Challenge definitions and progress tracking
  - Fields: mission, currentStampCount, currentOrangeCount, reward
  - Status: PENDING, PROGRESS, COMPLETE
- `ChallengeTemp` - Temporary storage for challenge modifications

**Controller Endpoints**:
- `/api/user/challenge` - Challenge management
- `/api/user/challenges` - List challenges
- `/api/user/challenge/complete` - Mark completion

**Key Features**:
- Orange points reward system
- Stamp collection mechanism
- Parent approval workflow

### 5. **Alarm Module**
**Primary Responsibility**: System-wide notification management

**Entities**:
- `Alarm` - System notifications
  - Types: Various alarm types for different events
  - Target: PARENT, CHILD, ALL
- `MemberAlarm` - User-specific alarm instances

**Integration Points**:
- Schedule alarms
- Challenge notifications
- Social interactions (follows, comments)

### 6. **Story Module**
**Primary Responsibility**: Social content sharing platform

**Sub-modules**:
- **story/story** - Main story content
  - `Story` entity with images, view counts
  - Regional tagging for local content
- **story/comment** - Comment system
- **story/like** - Like functionality
- **story/reply** - Reply to comments

**Controller Endpoints**:
- `/api/user/story` - Story CRUD
- `/api/user/story/comment` - Comment management
- `/api/user/story/like` - Like/unlike stories

**Key Features**:
- Image attachments
- View count tracking
- Regional content filtering
- Engagement metrics

### 7. **Pick Module**
**Primary Responsibility**: Magazine/article content management

**Structure** (Similar to Story):
- **pick/pick** - Main content
- **pick/comment** - Comments
- **pick/like** - Likes
- **pick/reply** - Replies

**Entity Features**:
- `Pick` - Magazine articles with categories
- Support for external links
- Admin-curated content

### 8. **Banner & Popup Modules**
**Primary Responsibility**: Marketing and announcement management

**Entities**:
- `Banner` / `BannerV2` - Promotional banners
- `Popup` - Pop-up announcements

**Features**:
- Time-based display
- Target audience selection
- Click tracking

### 9. **Follow & Cheering Modules**
**Primary Responsibility**: Social networking features

**Entities**:
- `Follow` - Following relationships
- `Cheering` - Support/encouragement system

**Features**:
- Bidirectional relationships
- Activity feeds
- Notification integration

### 10. **Supporting Modules**

**Location Module**:
- Location code management
- Address validation

**Report Module**:
- Content/user reporting system
- Moderation workflow

**Notice Module**:
- System announcements
- User notifications

**Terms Module**:
- Terms of service management
- User agreements

**Visitor Module**:
- Analytics and tracking
- User activity monitoring

## Common Patterns

1. **Repository Pattern**: Each module uses:
   - Standard JPA repository
   - Custom repository interface
   - QueryDSL implementation

2. **DTO Pattern**: Clear separation between:
   - Create DTOs
   - Update DTOs
   - Response DTOs
   - Search/Filter DTOs

3. **Entity Inheritance**: All entities extend `CommonEntity` with:
   - ID, createdDate, updatedDate
   - Soft delete support

4. **Security**: JWT-based authentication with role separation:
   - User endpoints: `/api/user/*`
   - Admin endpoints: `/api/admin/*`
   - Public endpoints: `/api/common/*`

## Key Business Flows

1. **Parent-Child Account Link**:
   - Parent creates account → Gets referral code
   - Child registers with parent's referral code
   - Parent approves child account

2. **Schedule Creation Flow**:
   - Create schedule → Generate calendar entries
   - Set up alarms for parent/child
   - Link to academy if applicable

3. **Challenge Workflow**:
   - Parent creates challenge
   - Child completes tasks → Earns stamps
   - Stamps convert to orange points
   - Parent approves completion

4. **Content Sharing**:
   - Users create stories with regional tags
   - Community engagement through likes/comments
   - Moderation through reporting system

## Integration Points

- **CommonMember** is the central entity connecting all modules
- **Schedule** integrates with Academy, Calendar, and Alarm modules
- **Story** and **Pick** share similar social features (comments, likes)
- **Alarm** system integrates across all major features
- Regional tagging connects Location, Story, and CommonMember modules

This architecture supports a comprehensive educational platform with strong social features, gamification elements, and robust scheduling capabilities.
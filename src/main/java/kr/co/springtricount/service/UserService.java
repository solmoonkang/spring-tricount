package kr.co.springtricount.service;

import kr.co.springtricount.infra.exception.DuplicatedException;
import kr.co.springtricount.persistence.entity.User;
import kr.co.springtricount.persistence.repository.UserRepository;
import kr.co.springtricount.service.dto.request.MemberReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void createUser(MemberReqDTO create) {

        checkIdentityExists(create.identity());

        final User user = User.toMemberEntity(create);

        userRepository.save(user);
    }

    private void checkIdentityExists(String identity) {
        if (userRepository.existsUserByIdentity(identity)) {
            throw new DuplicatedException("해당 아이디는 이미 존재하는 아이디입니다.");
        }
    }

}

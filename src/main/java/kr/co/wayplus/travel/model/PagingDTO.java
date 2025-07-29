package kr.co.wayplus.travel.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class PagingDTO {
    static int DEFAULT_PAGE_PER_SIZE = 10;
    static int DEFAULT_GROUP_PER_SIZE = 5;

    private int numberId;
    private String stringId;

    private int totalItemCount;
    private int itemStartPosition;
    private int itemEndPosition;
    private int indexStartNum;

    private int totalPageNumber;
    private int currentPageNumber;
    private int pagePerSize;

    private int totalGroupNumber;
    private int currentGroupNumber;
    private int groupPerSize;
    private int nextGroupNumber;
    private int prevGroupNumber;

    public PagingDTO(int totalItemCount, int currentPageNumber, int indexStartNum){
        this.totalItemCount = totalItemCount;
        this.currentPageNumber = currentPageNumber;
        this.pagePerSize = DEFAULT_PAGE_PER_SIZE;
        this.groupPerSize = DEFAULT_GROUP_PER_SIZE;
        this.indexStartNum = indexStartNum;
        generateTotalPageNumber();
        generateListPosition(totalItemCount);
        generateGroupPosition();
    }

    public PagingDTO(int totalItemCount, int currentPageNumber, int indexStartNum, int pagePerSize){
        this.totalItemCount = totalItemCount;
        this.currentPageNumber = currentPageNumber;
        this.pagePerSize = pagePerSize;
        this.groupPerSize = DEFAULT_GROUP_PER_SIZE;
        this.indexStartNum = indexStartNum;
        generateTotalPageNumber();
        generateListPosition(totalItemCount);
        generateGroupPosition();
    }

    public PagingDTO(int totalItemCount, int currentPageNumber, int indexStartNum, int pagePerSize, int groupPerSize){
        this.totalItemCount = totalItemCount;
        this.currentPageNumber = currentPageNumber;
        this.pagePerSize = pagePerSize;
        this.groupPerSize = groupPerSize;
        this.indexStartNum = indexStartNum;
        generateTotalPageNumber();
        generateListPosition(totalItemCount);
        generateGroupPosition();
    }

    private void generateTotalPageNumber(){
        if(totalItemCount % pagePerSize != 0){
            this.totalPageNumber = (totalItemCount / pagePerSize) + 1;
        }else{
            this.totalPageNumber = totalItemCount / pagePerSize;
        }
        if(this.totalPageNumber == 0) this.totalPageNumber = 1;
    }

    private void generateListPosition(int totalItemCount){
        if(totalItemCount > 0)
            this.itemStartPosition = currentPageNumber * pagePerSize - pagePerSize + indexStartNum;
        else this.itemStartPosition = 0;

        if(totalItemCount > 0)
            this.itemEndPosition = currentPageNumber * pagePerSize;
        else
            this.itemEndPosition = 0;
    }

    private void generateGroupPosition(){
        //GroupNo 0기준
        if((totalPageNumber % groupPerSize) != 0)
            this.totalGroupNumber = totalPageNumber/groupPerSize + 1;
        else
            this.totalGroupNumber = totalPageNumber/groupPerSize;

        if(totalGroupNumber <= 1){
            prevGroupNumber = 1;
            nextGroupNumber = 1;
            currentGroupNumber = 1;
        }else{
            float cgn = (float)currentPageNumber / (float)groupPerSize;
            currentGroupNumber = (int) Math.ceil(cgn);

            if(currentGroupNumber <= 1){
                prevGroupNumber = 1;
            }else {
                prevGroupNumber = currentGroupNumber - 1;
            }

            if(currentGroupNumber < totalGroupNumber){
                nextGroupNumber = currentGroupNumber + 1;
            } else{
                nextGroupNumber = currentGroupNumber;
            }
        }
    }

}
